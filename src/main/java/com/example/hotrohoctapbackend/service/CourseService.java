package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

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

            BigDecimal rate = (BigDecimal)row[8];
            CourseDTO courseSummary = new CourseDTO(id,danhmucID, title, imageUrl, price,cost, numberOfStudents, totalLessons,rate);
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


    public Page<CourseDTO> getAllCourse( Pageable pageable) {
        Page<Object[]> results = courseRepository.findAllCourses( pageable);
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
