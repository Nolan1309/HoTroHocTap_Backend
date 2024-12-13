package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.DTO.User.ReviewDTO_USER_POST;
import com.example.hotrohoctapbackend.entity.Review;
import com.example.hotrohoctapbackend.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/course")
    public ResponseEntity<?> AddReviewsByCourseIdUser(@RequestBody ReviewDTO_USER_POST reviewDTOUserPost) {
        try {
            // Gọi service để xử lý logic thêm đánh giá
            Review review = reviewService.postReviewUser(reviewDTOUserPost);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (RuntimeException e) {
            // Xử lý ngoại lệ nếu có (ví dụ: người dùng đã gửi đánh giá trước đó)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Xử lý ngoại lệ chung
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi trong quá trình thêm đánh giá.");
        }
    }


}
