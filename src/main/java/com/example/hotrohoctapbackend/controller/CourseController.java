package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.CourseDTO;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;




    @GetMapping("/course/top6")
    public List<CourseDTO> getLevel1Categories() {
        return courseService.getTop_6_CoursesWithDetails();
    }



}
