package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminDicountDetailDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscounAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscountGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.ApplyDiscountRequest;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.DiscountItemDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.DiscountItemDTOResponsive;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.enums.DiscountFormat;
import com.example.hotrohoctapbackend.enums.DiscountStatus;
import com.example.hotrohoctapbackend.enums.DiscountType;
import com.example.hotrohoctapbackend.enums.ExamType;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private Course_DiscountRepository courseDiscountRepository;

    @Autowired
    private ExamInfoRepository examInfoRepository;

    @Autowired
    private CourseBundleRepository courseBundleRepository;

    public Page<AdminDiscountGetDTO> getDiscounts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<Object[]> results = discountRepository.findDiscounts(pageable);

        return results.map(row -> new AdminDiscountGetDTO(
                (Integer) row[0],                            // id
                (String) row[1],                             // description                 // discount_type
                row[2] != null ? ((BigDecimal) row[2]).doubleValue() : 0.0,  // Chuyển BigDecimal sang Double
                (String) row[3],                             // title
                (Boolean) row[4]                             // is_deleted
        ));
    }

    public AdminDicountDetailDTO getDiscountById(Integer id) {
        List<Object[]> discountDataList = discountRepository.findDiscountById(id);
        if (discountDataList != null && !discountDataList.isEmpty()) {
            Object[] data = discountDataList.get(0);
            if (data != null && data.length >= 6) {
                int discountId = (Integer) data[0];
                String description = (String) data[1];
                double discountValue = ((BigDecimal) data[2]).doubleValue();
                String title = (String) data[3];
                LocalDateTime endDate = ((java.sql.Timestamp) data[4]).toLocalDateTime();
                LocalDateTime startDate = ((java.sql.Timestamp) data[5]).toLocalDateTime();

                return new AdminDicountDetailDTO(discountId, description, discountValue, title, endDate, startDate);
            }
        }
        return null;
    }

    public Discount hideDiscountAdmin(int discountID) {
        // Tìm tài khoản theo ID
        Optional<Discount> discount = discountRepository.findById(discountID);

        if (discount.isPresent()) {
            Discount account = discount.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return discountRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + discountID);
        }
    }

    public Discount showDiscountAdmin(int discountID) {
        // Tìm tài khoản theo ID
        Optional<Discount> accountOpt = discountRepository.findById(discountID);

        if (accountOpt.isPresent()) {
            Discount account = accountOpt.get();
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return discountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + discountID);
        }
    }


    @Transactional
    public ApiResponse<DiscountItemDTOResponsive> addDiscount(DiscountItemDTO discountDTO) {
        if (discountDTO.getCode() == null || discountDTO.getCode().isEmpty()) {
            return new ApiResponse<>(400, "Mã giảm giá không được trống.", null);
        }
        if (discountDTO.getDiscountType() == null || discountDTO.getDiscountType().isEmpty()) {
            return new ApiResponse<>(400, "Loại giảm giá không được thiếu.", null);
        }
        if (discountDTO.getStartDate() == null || discountDTO.getEndDate() == null) {
            return new ApiResponse<>(400, "Ngày bắt đầu và ngày kết thúc không được để trống.", null);
        }
        if (discountDTO.getStartDate().isAfter(discountDTO.getEndDate())) {
            return new ApiResponse<>(400, "Ngày bắt đầu phải nhỏ hơn ngày kết thúc.", null);
        }

        // Tạo discount -> phải có voucherType vói DiscountType
        Discount discount = new Discount();


        DiscountFormat discountFormat = DiscountFormat.valueOf(discountDTO.getDiscountType());
        discount.setFormat(discountFormat);


        discount.setCode(discountDTO.getCode());
        discount.setTitle(discountDTO.getTitle());
        discount.setDescription(discountDTO.getDescription());
        BigDecimal discountValue = new BigDecimal(discountDTO.getValue());
        discount.setDiscountValue(discountValue);
        discount.setStartedDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        if (discountDTO.getDiscountType().equals(DiscountFormat.VOUCHER.name())) {
            BigDecimal minOrder = BigDecimal.valueOf(discountDTO.getMinOrderValue());
            discount.setMinOrderValue(minOrder);
            discount.setMaxUsed(discountDTO.getMaxUsed());
            discount.setUsedCount(0);
        } else {
            DiscountType discountType = DiscountType.valueOf(discountDTO.getVoucherType());
            discount.setDiscountType(discountType);
        }
        discount.setStatus(DiscountStatus.ACTIVE);
        discount.setCreatedAt(LocalDateTime.now());
        discount.setUpdatedAt(LocalDateTime.now());
        discount.setDeleted(false);  // Đánh dấu là không bị xóa
        discount = discountRepository.save(discount);
        DiscountItemDTOResponsive addedDiscountDTO = convertToDTO(discount);
        return new ApiResponse<>(200, "Thêm giảm giá thành công.", addedDiscountDTO);
    }


    // Sửa Discount
    @Transactional
    public DiscountItemDTOResponsive updateDiscount(int discountId, DiscountItemDTO discountDTO) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount không tồn tại"));

        DiscountFormat discountFormat = DiscountFormat.valueOf(discountDTO.getDiscountType());
        discount.setFormat(discountFormat);

        if (discountDTO.getDiscountType() == DiscountFormat.VOUCHER.name()) {
            BigDecimal minOrder = BigDecimal.valueOf(discountDTO.getMinOrderValue());
            discount.setMinOrderValue(minOrder);
            discount.setMaxUsed(discountDTO.getMaxUsed());
        } else {
            discount.setDiscountType(DiscountType.valueOf(discountDTO.getVoucherType()));
        }


        discount.setCode(discountDTO.getCode());
        discount.setTitle(discountDTO.getTitle());
        discount.setDescription(discountDTO.getDescription());
        BigDecimal discountValue = new BigDecimal(discountDTO.getValue());
        discount.setDiscountValue(discountValue);
        discount.setStartedDate(discountDTO.getStartDate());
        discount.setEndDate(discountDTO.getEndDate());
        discount.setUpdatedAt(LocalDateTime.now());


        // Cập nhật Discount vào cơ sở dữ liệu
        discount = discountRepository.saveAndFlush(discount);

        // Chuyển đổi thành DTO trả về
        return convertToDTO(discount);
    }


    // Phương thức phân trang và chuyển đổi sang DTO
    public Page<DiscountItemDTOResponsive> getDiscountsWithPagination(String title, String format, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);  // Đặt page và size
        DiscountFormat discountFormat = null;
        if (format != null && !format.isEmpty()) {
            discountFormat = DiscountFormat.valueOf(format.toUpperCase());
        }

        Page<Discount> discountPage = discountRepository.findByTitleAndDiscountType(title, discountFormat, pageable);  // Lấy trang từ repository
        return discountPage.map(this::convertToDTO);  // Chuyển mỗi Discount thành DTO
    }

    // Phương thức chuyển đổi từ Discount entity thành DiscountItemDTOResponsive
    private DiscountItemDTOResponsive convertToDTO(Discount discount) {
        DiscountItemDTOResponsive dto = new DiscountItemDTOResponsive();
        dto.setId(String.valueOf(discount.getId()));
        dto.setCode(discount.getCode());
        dto.setTitle(discount.getTitle());
        dto.setDiscountType(discount.getFormat().name());

        if (discount.getFormat() == DiscountFormat.VOUCHER) {
            Integer minOrderValue = (discount.getMinOrderValue() != null)
                    ? discount.getMinOrderValue().intValue()
                    : 0;
            dto.setMinOrderValue(minOrderValue);
            dto.setMaxUsed(discount.getMaxUsed());
            dto.setUsedCount(discount.getUsedCount());
        }
        dto.setDescription(discount.getDescription());
        dto.setValue(discount.getDiscountValue().toString());
        dto.setStartDate(discount.getStartedDate());
        dto.setEndDate(discount.getEndDate());
        dto.setStatus(discount.getStatus().name());
        dto.setCreatedAt(discount.getCreatedAt());
        dto.setUpdatedAt(discount.getUpdatedAt());
        dto.setDeletedAt(discount.getDeletedDate());
        dto.setIsDeleted(discount.isDeleted());
        if (discount.getFormat() == DiscountFormat.DISCOUNT) {
            if (discount.getDiscountType() == DiscountType.COURSE) {
                List<Integer> courseIdsByDiscountIdAndType = courseDiscountRepository.findCourseIdsByDiscountIdAndType(discount.getId(), discount.getDiscountType());
                dto.setTargetIds(courseIdsByDiscountIdAndType);
            } else {
                List<Integer> courseIdsByDiscountIdAndType = courseDiscountRepository.findTestIdsByDiscountIdAndType(discount.getId(), discount.getDiscountType());
                dto.setTargetIds(courseIdsByDiscountIdAndType);
            }
            dto.setVoucherType(discount.getDiscountType().name());
        }
        return dto;
    }


    public ApiResponse<String> getLatestDiscountCode() {
        Discount discount = discountRepository.findTopByOrderByCreatedAtDesc();
        if (discount != null) {
            String updatedCode = increaseDiscountCode(discount.getCode());
            return new ApiResponse<>(200, "Tạo mã giảm giá thành công.", updatedCode);
        } else {
            String newCode = generateNewDiscountCode();
            return new ApiResponse<>(200, "Không có mã giảm giá, tạo mã mới.", newCode);
        }
    }

    private String increaseDiscountCode(String currentCode) {
        String prefix = currentCode.substring(0, 10);
        String numberPart = currentCode.substring(10);
        int number = Integer.parseInt(numberPart);
        number++;
        String newNumberPart = String.format("%03d", number);
        return prefix + newNumberPart;
    }

    private String generateNewDiscountCode() {
        long count = discountRepository.count();
        String newCode = "SALE-2025-" + String.format("%03d", count + 1);
        return newCode;
    }

    // Bật tắt trạng thái Mã giảm
    public ApiResponse<String> toggleDiscountStatus(int discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã giảm giá"));
        List<Course_Discount> courseDiscountList = courseDiscountRepository.findByDiscountId(discountId);

        if (discount.getStatus() == DiscountStatus.ACTIVE) {
            if (discount.getDiscountType() == DiscountType.COURSE) {
                for (Course_Discount item : courseDiscountList) {
                    Course course = courseRepository.findById(item.getCourse().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + item.getCourse().getId()));
                    UpdatePriceCourseRemove(course);
                    courseRepository.saveAndFlush(course);
                }
            } else if (discount.getDiscountType() == DiscountType.COMBO) {
                for (Course_Discount item : courseDiscountList) {
                    CourseBundle course = courseBundleRepository.findById(item.getCourseBundle().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy combo với ID: " + item.getCourseBundle().getId()));
                    UpdatePriceCourseBundleRemove(course, discount.getDiscountValue());
                    courseBundleRepository.saveAndFlush(course);
                }
            } else {
                for (Course_Discount item : courseDiscountList) {
                    ExamInfo examInfo = examInfoRepository.findByTestId(item.getTest().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Exam "));
                    UpdatePriceTestRemove(examInfo);
                    examInfoRepository.saveAndFlush(examInfo);
                }
            }
            discount.setStatus(DiscountStatus.DISABLED);
        } else {
            if (discount.getDiscountType() == DiscountType.COURSE) {
                for (Course_Discount item : courseDiscountList) {
                    Course course = courseRepository.findById(item.getCourse().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + item.getCourse().getId()));
                    UpdatePriceCourse(discount.getDiscountValue(), course);
                    courseRepository.saveAndFlush(course);
                }
            } else if (discount.getDiscountType() == DiscountType.COMBO) {
                for (Course_Discount item : courseDiscountList) {
                    CourseBundle courseBundle = courseBundleRepository.findById(item.getCourseBundle().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Combo với ID: " + item.getCourseBundle().getId()));
                    UpdatePriceCourseBundle(courseBundle, discount.getDiscountValue());
                    courseBundleRepository.saveAndFlush(courseBundle);
                }
            } else {
                for (Course_Discount item : courseDiscountList) {
                    ExamInfo examInfo = examInfoRepository.findByTestId(item.getTest().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Exam "));
                    UpdatePriceTest(discount.getDiscountValue(), examInfo);
                    examInfoRepository.saveAndFlush(examInfo);
                }
            }
            discount.setStatus(DiscountStatus.ACTIVE);
        }
        discountRepository.save(discount);
        return new ApiResponse<>(200, "Toggle status thành công.", discount.getStatus().name());
    }

    //Logic : Áp dụng thì phải ở trạng thái Tắt , Chưa mở Giảm giá . COURSE / TEST / COMBO
    public ApiResponse<String> applyDiscount(ApplyDiscountRequest request) {
        // Lấy mã giảm giá từ discountId
        Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mã giảm giá"));

        if ("COURSE".equalsIgnoreCase(request.getVoucherType())) {
            for (Integer courseId : request.getTargetIds()) {
                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId));


                Course_Discount existingCourseDiscount = courseDiscountRepository.findByCourseAndDiscount(course, discount);
                if (existingCourseDiscount != null) {
                    existingCourseDiscount.setStatus(true);

                    courseDiscountRepository.saveAndFlush(existingCourseDiscount);
                } else {
                    // Nếu chưa có, tạo bản ghi mới
                    Course_Discount courseDiscount = new Course_Discount();
                    courseDiscount.setCourse(course);
                    courseDiscount.setDiscount(discount);
                    courseDiscount.setDiscountType(DiscountType.COURSE);
                    courseDiscount.setStatus(true);
                    courseDiscountRepository.save(courseDiscount);
                }

            }
            List<Integer> currentCourseIds = request.getTargetIds();
            List<Course_Discount> existingCourseDiscounts = courseDiscountRepository.findByDiscount(discount);
            for (Course_Discount courseDiscount : existingCourseDiscounts) {
                if (!currentCourseIds.contains(courseDiscount.getCourse().getId())) {
                    courseDiscountRepository.delete(courseDiscount);
                }
            }
        } else if ("TEST".equalsIgnoreCase(request.getVoucherType())) {
            for (Integer testId : request.getTargetIds()) {
                Test test = testRepository.findById(testId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài kiểm tra với ID: " + testId));

                Course_Discount existingTestDiscount = courseDiscountRepository.findByTestAndDiscount(test, discount);
                if (existingTestDiscount != null) {

                    existingTestDiscount.setStatus(true);
                    courseDiscountRepository.saveAndFlush(existingTestDiscount);
                } else {
                    // Nếu chưa có, tạo bản ghi mới
                    Course_Discount courseDiscount = new Course_Discount();
                    courseDiscount.setTest(test);
                    courseDiscount.setDiscount(discount);
                    courseDiscount.setDiscountType(DiscountType.TEST);
                    courseDiscount.setStatus(true);
                    courseDiscountRepository.save(courseDiscount);
                }

            }
            List<Integer> currentTestIds = request.getTargetIds();
            List<Course_Discount> existingTestDiscounts = courseDiscountRepository.findByDiscount(discount);
            for (Course_Discount courseDiscount : existingTestDiscounts) {
                if (!currentTestIds.contains(courseDiscount.getTest().getId())) {
                    courseDiscountRepository.delete(courseDiscount);
                }
            }
        } else if ("COMBO".equalsIgnoreCase(request.getVoucherType())) {
            for (Integer courseBundleId : request.getTargetIds()) {
                CourseBundle courseBundle = courseBundleRepository.findById(courseBundleId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy combo với ID: " + courseBundleId));

                Course_Discount existingTestDiscount = courseDiscountRepository.findByCourseBundleAndDiscount(courseBundle, discount);
                if (existingTestDiscount != null) {

                    existingTestDiscount.setStatus(true);
                    courseDiscountRepository.saveAndFlush(existingTestDiscount);
                } else {
                    // Nếu chưa có, tạo bản ghi mới
                    Course_Discount courseDiscount = new Course_Discount();
                    courseDiscount.setCourseBundle(courseBundle);
                    courseDiscount.setDiscount(discount);
                    courseDiscount.setDiscountType(DiscountType.COMBO);
                    courseDiscount.setStatus(true);
                    courseDiscountRepository.save(courseDiscount);
                }

            }
            List<Integer> currentTestIds = request.getTargetIds();
            List<Course_Discount> existingTestDiscounts = courseDiscountRepository.findByDiscount(discount);
            for (Course_Discount courseDiscount : existingTestDiscounts) {
                if (!currentTestIds.contains(courseDiscount.getCourseBundle().getId())) {
                    courseDiscountRepository.delete(courseDiscount);
                }
            }
        } else {
            throw new IllegalArgumentException("Loại giảm giá không hợp lệ.");
        }

        // Trả về phản hồi thành công
        return new ApiResponse<>(200, "Áp dụng mã giảm giá thành công.", null);
    }

    public boolean UpdatePriceCourse(BigDecimal percentDiscount, Course course) {
        BigDecimal percentDecimal = percentDiscount.divide(BigDecimal.valueOf(100));
        BigDecimal finalPrice = course.getCost().multiply(BigDecimal.ONE.subtract(percentDecimal));
        course.setPrice(finalPrice);
        return true;
    }

    public boolean UpdatePriceCourseBundle(CourseBundle courseBundle, BigDecimal percentDiscount) {
        double discountPercentageValue = percentDiscount.doubleValue();
        double discountedPrice = courseBundle.getPrice().doubleValue() * (1 - (discountPercentageValue / 100));
        courseBundle.setPrice(new BigDecimal(discountedPrice));
        return true;
    }

    public boolean UpdatePriceTest(BigDecimal percentDiscount, ExamInfo test) {
        BigDecimal percentDecimal = percentDiscount.divide(BigDecimal.valueOf(100));
        BigDecimal finalPrice = test.getCost().multiply(BigDecimal.ONE.subtract(percentDecimal));
        test.setPrice(finalPrice);
        return true;
    }

    public boolean UpdatePriceCourseRemove(Course course) {
        course.setPrice(course.getCost());
        return true;
    }

    public boolean UpdatePriceCourseBundleRemove(CourseBundle courseBundle, BigDecimal discountPercentage) {
        double discountedPrice = courseBundle.getPrice().doubleValue();
        double percentage = discountPercentage.doubleValue();
        double originalPrice = discountedPrice / (1 - (percentage / 100));
        courseBundle.setPrice(new BigDecimal(originalPrice));
        return true;
    }


    public boolean UpdatePriceTestRemove(ExamInfo test) {
        test.setPrice(test.getCost());
        return true;
    }
}
