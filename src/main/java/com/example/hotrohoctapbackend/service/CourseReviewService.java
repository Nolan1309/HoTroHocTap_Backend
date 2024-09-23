package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.dao.ReviewRepository;
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
}
