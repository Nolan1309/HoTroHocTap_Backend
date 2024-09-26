package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.entity.Review;
import com.example.hotrohoctapbackend.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private CourseReviewService reviewService;
    @GetMapping("/course/{courseId}")
    public List<ReviewDTO> getReviewsByCourseId(@PathVariable Integer courseId) {
        return reviewService.getReviewsByCourseId(courseId);
    }
}