package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.Course_DiscountRepository;
import com.example.hotrohoctapbackend.dao.DiscountRepository;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Course_Discount;
import com.example.hotrohoctapbackend.entity.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseDiscountService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private Course_DiscountRepository courseDiscountRepository;

    @Autowired
    private DiscountRepository discountRepository;

    public String addDiscountToCourses(Integer discountId, List<Integer> courseIds) {
        // Kiểm tra nếu danh sách khóa học rỗng
        if (courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ID khóa học không được để trống.");
        }

        // 1. Kiểm tra khuyến mãi có tồn tại hay không
        Optional<Discount> optionalDiscount = discountRepository.findById(discountId);
        if (optionalDiscount.isEmpty()) {
            throw new IllegalArgumentException("Khuyến mãi không tồn tại với ID: " + discountId);
        }
        Discount discount = optionalDiscount.get();

        // Lấy giá trị giảm giá từ Discount
        BigDecimal discountValue = discount.getDiscount_value();
        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá trị giảm giá không hợp lệ.");
        }

        StringBuilder responseBuilder = new StringBuilder("Kết quả thêm khuyến mãi:\n");

        // 2. Lặp qua danh sách khóa học
        for (Integer courseId : courseIds) {
            try {

                Integer exist = courseDiscountRepository.existsByCourseAndDiscountAndNotDeleted(courseId, discountId);
                Optional<Course> optionalCourse = courseRepository.findById(courseId);
                if (optionalCourse.isEmpty()) {
                    responseBuilder.append("Khóa học với ID ").append(courseId).append(" không tồn tại.\n");
                    continue;
                }
                Course course = optionalCourse.get();

                // Tính giá mới
                BigDecimal currentPrice = course.getCost();
                if (currentPrice == null) {
                    responseBuilder.append("Khóa học với ID ").append(courseId)
                            .append(" có giá không hợp lệ.\n");
                    continue;
                }

                // Kiểm tra discountValue có nằm trong khoảng hợp lệ (0 - 100%)
                if (discountValue.compareTo(BigDecimal.ZERO) < 0 || discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {
                    responseBuilder.append("Giảm giá không hợp lệ cho khóa học ID: ").append(courseId).append("\n");
                    continue;
                }

                // Tính giá trị giảm giá từ phần trăm
                BigDecimal discountAmount = currentPrice.multiply(discountValue).divide(BigDecimal.valueOf(100));
                BigDecimal newPrice = currentPrice.subtract(discountAmount);

                if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
                    responseBuilder.append("Giá mới không hợp lệ cho khóa học ID: ").append(courseId).append("\n");
                    continue;
                }

                // Cập nhật giá mới
                course.setPrice(newPrice);
                courseRepository.save(course);
                if (exist > 0) {
                    Optional<Course_Discount> optionalCourseDiscount = courseDiscountRepository.findByCourseIdAndDiscountId(courseId, discountId);
                    if (optionalCourseDiscount.isPresent()) {
                        optionalCourseDiscount.get().setCheck(true);
                        courseDiscountRepository.save(optionalCourseDiscount.get());
                    }
                } else {
                    // Lưu vào bảng course_discounts
                    Course_Discount courseDiscount = new Course_Discount();
                    courseDiscount.setCourse(course);
                    courseDiscount.setDiscount(discount);
                    courseDiscount.setDeletedDate(LocalDateTime.now());
                    courseDiscount.setDeleted(false);
                    courseDiscount.setCheck(true);
                    courseDiscountRepository.save(courseDiscount);

                    responseBuilder.append("Thành công thêm khuyến mãi cho khóa học ID: ").append(courseId).append("\n");
                }
                // Lấy thông tin khóa học

            } catch (Exception ex) {
                // Ghi lại lỗi với từng khóa học
                responseBuilder.append("Lỗi không mong muốn với khóa học ID: ").append(courseId)
                        .append(" - ").append(ex.getMessage()).append("\n");
            }
        }

        return responseBuilder.toString();
    }

    public String resetPriceToCost(List<Integer> courseIds) {
        // Kiểm tra nếu danh sách khóa học rỗng
        if (courseIds == null || courseIds.isEmpty()) {
            throw new IllegalArgumentException("Danh sách ID khóa học không được để trống.");
        }

        StringBuilder responseBuilder = new StringBuilder("Kết quả cập nhật giá:\n");

        // 1. Lặp qua danh sách khóa học
        for (Integer courseId : courseIds) {
            try {
                // Lấy thông tin khóa học
                Optional<Course> optionalCourse = courseRepository.findById(courseId);
                if (optionalCourse.isEmpty()) {
                    responseBuilder.append("Khóa học với ID ").append(courseId).append(" không tồn tại.\n");
                    continue;
                }
                Course course = optionalCourse.get();

                // Lấy chi phí gốc (cost) và kiểm tra hợp lệ
                BigDecimal cost = course.getCost();
                if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) {
                    responseBuilder.append("Khóa học với ID ").append(courseId)
                            .append(" có chi phí gốc không hợp lệ.\n");
                    continue;
                }

                // Cập nhật giá bằng với chi phí gốc
                course.setPrice(cost);
                courseRepository.save(course);
                List<Object[]> results = courseDiscountRepository.findCourseDiscountsByCourseId(courseId);
                for (Object[] row : results) {
                    Integer id = (Integer) row[0];
                    Object obj = row[1];  // Lấy đối tượng từ mảng row
                    LocalDateTime deletedDate = null;

                    if (obj instanceof Timestamp) {
                        Timestamp timestamp = (Timestamp) obj;
                        deletedDate = timestamp.toLocalDateTime();  // Chuyển đổi từ Timestamp sang LocalDateTime
                    }

                    boolean isDeleted = (boolean) row[2];
                    Integer courseIdD = (Integer) row[3];
                    Integer discountId = (Integer) row[4];
                    boolean isCheck = (boolean) row[5];
                    if (isCheck) {
                        courseDiscountRepository.updateIsCheckToFalse(id);
                    }
                }
                responseBuilder.append("Thành công cập nhật giá cho khóa học ID: ").append(courseId).append("\n");
            } catch (Exception ex) {
                // Ghi lại lỗi với từng khóa học
                responseBuilder.append("Lỗi không mong muốn với khóa học ID: ").append(courseId)
                        .append(" - ").append(ex.getMessage()).append("\n");
            }
        }

        return responseBuilder.toString();
    }


}
