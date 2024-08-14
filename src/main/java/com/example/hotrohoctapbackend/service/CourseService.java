package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
}
