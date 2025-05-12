package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Evalution.EvaluationDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Evalution.ReviewUpdateRequest;
import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.DTO.User.ReviewDTO_USER_POST;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.ReviewRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Review;
import com.example.hotrohoctapbackend.enums.ReviewStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Page<EvaluationDTO> getCourseReviews(String keyword, Integer courseId, String status, Integer rating, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findCourseReviews(keyword, courseId, status, rating, pageable);
        return reviews.map(this::mapToDTO);
    }

    public Page<EvaluationDTO> getTestReviews(String keyword, Integer courseId, Integer testId, String status, Integer rating, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findTestReviews(keyword, courseId, testId, status, rating, pageable);
        return reviews.map(this::mapToDTO);
    }


    private EvaluationDTO mapToDTO(Review review) {
        EvaluationDTO dto = new EvaluationDTO();
        dto.setId(review.getId());

        if (review.getCourse() != null) {
            dto.setCourseId(review.getCourse().getId());
            dto.setCourseName(review.getCourse().getTitle());
            dto.setReviewType("COURSE");
        }

        if (review.getTest() != null) {
            dto.setTestId(review.getTest().getId());
            dto.setTestName(review.getTest().getTitle());
            dto.setReviewType("TEST");
        }

        dto.setAccountId(review.getAccount().getId());
        dto.setAccountName(review.getAccount().getFullname());
        dto.setRating(review.getRating());
        dto.setReview(review.getReview());
        dto.setCreatedAt(review.getCreated_at() != null ? review.getCreated_at().toString() : null);
        dto.setUpdatedAt(review.getUpdated_at() != null ? review.getUpdated_at().toString() : null);
        dto.setDeletedDate(review.getDeletedDate() != null ? review.getDeletedDate().toString() : null);
        dto.setDeleted(review.isDeleted());
        dto.setStatus(review.getStatus().name().toLowerCase());

        return dto;
    }


    public Page<ReviewDTO> getReviewsByCourseId(Integer courseId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByCourseId(courseId, pageable);
        Page<ReviewDTO> reviewDTOs = reviews.map(review -> {
            Account account = review.getAccount();
            return new ReviewDTO(
                    review.getId(),
                    review.getCreated_at(),
                    review.getRating(),
                    review.getReview(),
                    review.getUpdated_at(),
                    account.getId(),
                    review.getCourse().getId(),
                    account.getFullname(),
                    account.getImage(),
                    null
            );
        });
        return reviewDTOs;
    }

    public Page<ReviewDTO> getReviewsByTestId(Integer testId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByTestId(testId, pageable);
        Page<ReviewDTO> reviewDTOs = reviews.map(review -> {
            Account account = review.getAccount();
            return new ReviewDTO(
                    review.getId(),
                    review.getCreated_at(),
                    review.getRating(),
                    review.getReview(),
                    review.getUpdated_at(),
                    account.getId(),
                    null,
                    account.getFullname(),
                    account.getImage(),
                    review.getTest().getId()
            );
        });
        return reviewDTOs;
    }

    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;  // or handle appropriately
    }

    public Review postReviewUser(ReviewDTO_USER_POST reviewDTOUserPost) {
        // Kiểm tra xem người dùng đã gửi đánh giá cho khóa học này chưa
        Review exist = reviewRepository.findByCourseIdAndAccountId(
                reviewDTOUserPost.getCourse_id(),
                reviewDTOUserPost.getAccount_id()
        );
        if (exist != null) {
            throw new RuntimeException("Người dùng đã gửi đánh giá cho khóa học này.");
        }

        // Tạo mới một đánh giá
        Review review = new Review();

        Course course = courseRepository.findById(reviewDTOUserPost.getCourse_id()).orElseThrow();
        Account account = accountRepository.findById(reviewDTOUserPost.getAccount_id()).orElseThrow();
        review.setCourse(course);
        review.setAccount(account);
        review.setRating(reviewDTOUserPost.getRating());
        review.setReview(reviewDTOUserPost.getReview());
        review.setCreated_at(LocalDateTime.now());
        review.setUpdated_at(LocalDateTime.now());

        // Lưu đánh giá vào database
        return reviewRepository.save(review);
    }

    public void updateReviewByAdmin(Integer id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Cập nhật nội dung đánh giá
        if (request.getReview() != null) {
            review.setReview(request.getReview());
        }

        // Cập nhật trạng thái đánh giá
        if (request.getStatus() != null) {
            try {
                review.setStatus(ReviewStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Trạng thái không hợp lệ");
            }
        }

        review.setUpdated_at(LocalDateTime.now());
        reviewRepository.save(review);
    }

    public void deleteReviewPermanently(Integer id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá cần xóa"));

        reviewRepository.delete(review); // Xóa vĩnh viễn khỏi DB
    }


}
