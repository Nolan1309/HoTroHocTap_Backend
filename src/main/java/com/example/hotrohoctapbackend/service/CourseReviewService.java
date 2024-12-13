package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.DTO.User.ReviewDTO_USER_POST;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.ReviewRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<ReviewDTO> getReviewsByCourseId(Integer courseId) {
        List<Object[]> results = reviewRepository.findByCourseId(courseId);

        // Mapping từ Object[] sang ReviewDTO theo thứ tự trả về từ MySQL
        return results.stream().map(obj -> new ReviewDTO(
                (Integer) obj[0],  // id
                convertToLocalDateTime(obj[1]), // created_at
                (Integer) obj[2],  // rating
                (String) obj[3],   // review
                convertToLocalDateTime(obj[4]), // updated_at
                (Integer) obj[5],  // account_id
                (Integer) obj[6],
                (String) obj[7],
                (String) obj[8]
        )).collect(Collectors.toList());
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

}
