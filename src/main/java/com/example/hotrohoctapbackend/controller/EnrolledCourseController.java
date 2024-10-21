package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.CountCourseDTO;
import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCourse(@RequestParam Integer accountId, @RequestParam Integer courseId) {
        try {
            String result = enrolledCourseService.enrollInCourse(accountId, courseId);

            if ("Actived Faild".equals(result)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/count/{accountId}")
    public ResponseEntity<CountCourseDTO> enrollCourseCount(@PathVariable Integer accountId) {
        try {
            CountCourseDTO result = enrolledCourseService.getEnrolledCoursesByAccountId(accountId);

            if (result.getTotalCourse() <= 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);  // If no courses, return 204 No Content
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }





}
