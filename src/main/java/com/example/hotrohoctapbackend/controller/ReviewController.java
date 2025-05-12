package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Evalution.EvaluationDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Evalution.ReviewUpdateRequest;
import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.DTO.User.ReviewDTO_USER_POST;
import com.example.hotrohoctapbackend.entity.Review;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
public class ReviewController {

    @Autowired
    private CourseReviewService reviewService;


    @GetMapping("/course/{courseId}")
    public ApiResponse<Page<ReviewDTO>> getReviewsByCourseId(@PathVariable Integer courseId, Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByCourseId(courseId, pageable);
        return new ApiResponse<>(200, "Success", reviews);
    }

    @GetMapping("/exam/{testId}")
    public ApiResponse<Page<ReviewDTO>> getReviewsByExamId(@PathVariable Integer testId, Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getReviewsByTestId(testId, pageable);
        return new ApiResponse<>(200, "Success", reviews);
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

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<EvaluationDTO>>> getReviews(
            @RequestParam String reviewType, // COURSE | TEST
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer testId, // thêm vào để filter theo đề thi
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<EvaluationDTO> result;

            switch (reviewType.toUpperCase()) {
                case "COURSE":
                    result = reviewService.getCourseReviews(keyword, courseId, status, rating, page, size);
                    break;
                case "TEST":
                    result = reviewService.getTestReviews(keyword, courseId, testId, status, rating, page, size);
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(400, "Invalid reviewType", null));
            }

            return ResponseEntity.ok(new ApiResponse<>(200, "Success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Server error: " + e.getMessage(), null));
        }
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<String>> updateReviewByAdmin(
            @PathVariable Integer id,
            @RequestBody ReviewUpdateRequest request
    ) {
        try {
            reviewService.updateReviewByAdmin(id, request);
            return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Lỗi khi cập nhật: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<String>> deleteReviewPermanently(@PathVariable Integer id) {
        try {
            reviewService.deleteReviewPermanently(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Xóa đánh giá thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Lỗi khi xóa: " + e.getMessage(), null));
        }
    }


}
