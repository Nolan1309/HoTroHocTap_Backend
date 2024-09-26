package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.Enrolled_CoursesRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import com.example.hotrohoctapbackend.entity.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnrolledCourseService {
    @Autowired
    private Enrolled_CoursesRepository enrolledCoursesRepository;

    public String enrollInCourse(Integer accountId, Integer courseId) {

        if (isUserEnrolled(accountId.longValue(), courseId.longValue())) {
            return "Actived Faild";
        }

        // Nếu chưa đăng ký, tiến hành đăng ký
        Enrolled_Courses enrolledCourses = new Enrolled_Courses();
        Account account = new Account();
        account.setId(accountId);

        RoleUser role = new RoleUser();
        role.setId(2);
        role.setRoleName("USER");
        account.setRole(role);

        enrolledCourses.setAccount(account);

        Course course = new Course();
        course.setId(courseId);
        enrolledCourses.setCourse(course);
        enrolledCourses.setEnrollmentDate(LocalDateTime.now());
        enrolledCourses.setStatus("Actived");

        enrolledCoursesRepository.save(enrolledCourses);
        return "Actived Success";
    }
    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrolledCoursesRepository.findEnrolledCourse(userId, courseId).isPresent();
    }
}
