package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/enrolled-course")
public class EnrolledCourseController {

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @GetMapping("/check-enrollment")
    public String checkUserEnrollment(@RequestParam Long userId, @RequestParam Long courseId) {
        boolean isEnrolled = enrolledCourseService.isUserEnrolled(userId, courseId);
        if (isEnrolled) {
            return "Actived";
        } else {
            return "NoActived";
        }
    }
}
