package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.Enrolled_CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrolledCourseService {
    @Autowired
    private Enrolled_CoursesRepository enrolledCoursesRepository;


    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrolledCoursesRepository.findEnrolledCourse(userId, courseId).isPresent();
    }
}
