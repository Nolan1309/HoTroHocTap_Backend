package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;


    // Lấy type của khóa học theo ID
    public String getCourseTypeById(int id) {
        return courseRepository.findCourseTypeById(id);
    }

    public Map<String, Integer> getCourseStatistics(Integer courseId) {
        // Lấy kết quả từ repository dưới dạng List<Object[]>
        List<Object[]> resultList = courseRepository.getCourseStatistics(courseId);

        // Khởi tạo Map để chứa kết quả
        Map<String, Integer> statistics = new HashMap<>();

        // Kiểm tra nếu kết quả không rỗng và có chứa giá trị
        if (!resultList.isEmpty()) {
            Object[] result = resultList.get(0);  // Lấy dòng đầu tiên vì chỉ có một kết quả

            // Kiểm tra và ép kiểu từng phần tử của Object[]
            if (result[0] instanceof Number) {
                statistics.put("total_students", ((Number) result[0]).intValue());
            }
            if (result[1] instanceof Number) {
                statistics.put("total_lessons", ((Number) result[1]).intValue());
            }
        }

        return statistics;
    }
    public CourseDetailDTO getCourseDetailById(Integer id) {
        List<Object[]> result = courseRepository.findCourseById(id);

        // Kiểm tra xem có dữ liệu không
        if (!result.isEmpty()) {
            Object[] data = result.get(0); // Lấy dòng đầu tiên
            CourseDetailDTO courseDetailDTO = new CourseDetailDTO();

            // Ánh xạ các phần tử của Object[] vào CourseDetailDTO
            if (data[0] instanceof Integer) {
                courseDetailDTO.setId((Integer) data[0]);
            }
            if (data[1] instanceof String) {
                courseDetailDTO.setAuthor((String) data[1]);
            }
            if (data[2] instanceof BigDecimal) {
                courseDetailDTO.setCost((BigDecimal) data[2]);
            }
            if (data[3] instanceof String) {
                courseDetailDTO.setCourseOutput((String) data[3]);
            }
            if (data[4] instanceof Timestamp) {
                courseDetailDTO.setCreatedAt(convertToLocalDateTime(data[4]));
            }
            if (data[5] instanceof String) {
                courseDetailDTO.setDescription((String) data[5]);
            }
            if (data[6] instanceof String) {
                courseDetailDTO.setDuration((String) data[6]);
            }
            if (data[7] instanceof String) {
                courseDetailDTO.setImage_url((String) data[7]);
            }
            if (data[8] instanceof String) {
                courseDetailDTO.setLanguage((String) data[8]);
            }
            if (data[9] instanceof BigDecimal) {
                courseDetailDTO.setPrice((BigDecimal) data[9]);
            }
            if (data[10] instanceof Boolean) {
                courseDetailDTO.setStatus((Boolean) data[10]);
            }
            if (data[11] instanceof String) {
                courseDetailDTO.setTitle((String) data[11]);
            }
            if (data[12] instanceof Timestamp) {
                courseDetailDTO.setUpdatedAt(convertToLocalDateTime(data[12]));
            }
            if (data[13] instanceof Integer) {
                courseDetailDTO.setCourse_category_id((Integer) data[13]);
            }

            return courseDetailDTO;
        }
        return null; // Xử lý khi không tìm thấy dữ liệu
    }

    // Helper function to convert Object to LocalDateTime
    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

    public List<CourseDTO> getTop_6_CoursesWithDetails() {
        List<Object[]> results = courseRepository.findTopCoursesWithDetails();

        List<CourseDTO> courseSummaries = new ArrayList<>();
        for (Object[] row : results) {
            Integer id = (Integer) row[0];
            Integer danhmucID = (Integer) row[1];

            String imageUrl = (String) row[2];
            BigDecimal price = (BigDecimal) row[3];
            BigDecimal cost = (BigDecimal) row[4];
            String title = (String) row[5];
            Long numberOfStudents = (Long) row[6];
            Long totalLessons = (Long) row[7];

            BigDecimal rate = (BigDecimal) row[8];
            CourseDTO courseSummary = new CourseDTO(id, danhmucID, title, imageUrl, price, cost, numberOfStudents, totalLessons, rate);
            courseSummaries.add(courseSummary);
        }

        return courseSummaries;
    }


    public Page<CourseDTO> getCoursesByCategory(Integer courseCategoryId, Pageable pageable) {
        Page<Object[]> results = courseRepository.findByCourseCategoryId(courseCategoryId, pageable);
        return results.map(row -> new CourseDTO(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                (BigDecimal) row[4],
                (String) row[5],
                (Long) row[6],
                (Long) row[7],
                (BigDecimal) row[8],
                (String) row[9],
                (String) row[10],
                (Date) row[11],
                (Date) row[12],
                (String) row[13],
                (String) row[14],
                (String) row[15],
                (Boolean) row[16]
        ));
    }

    public Page<CourseDTO> getCoursesByCategories(List<Integer> courseCategoryIds, Pageable pageable) {
        // Lấy kết quả từ repository
        Page<Object[]> results = courseRepository.findByCourseCategoryIds(courseCategoryIds, pageable);

        // Chuyển đổi từ Object[] sang CourseDTO
        return results.map(row -> new CourseDTO(
                (Integer) row[0], // course_id
                (Integer) row[1], // course_category_id
                (String) row[2],  // image_url
                (BigDecimal) row[3], // price
                (BigDecimal) row[4], // cost
                (String) row[5],  // course_title
                (Long) row[6], // number_of_students
                (Long) row[7], // total_lessons
                (BigDecimal) row[8], // average_rating
                (String) row[9],  // author
                (String) row[10], // course_output
                (Date) row[11], // created_at
                (Date) row[12], // updated_at
                (String) row[13], // description
                (String) row[14], // duration
                (String) row[15], // language
                (Boolean) row[16] // status
        ));
    }


    public Page<CourseDTO> getAllCourse(Pageable pageable) {
        Page<Object[]> results = courseRepository.findAllCourses(pageable);
        return results.map(row -> new CourseDTO(
                (Integer) row[0],
                (Integer) row[1],
                (String) row[2],
                (BigDecimal) row[3],
                (BigDecimal) row[4],
                (String) row[5],
                (Long) row[6],
                (Long) row[7],
                (BigDecimal) row[8],
                (String) row[9],
                (String) row[10],
                (Date) row[11],
                (Date) row[12],
                (String) row[13],
                (String) row[14],
                (String) row[15],
                (Boolean) row[16]
        ));
    }

}
