package com.example.hotrohoctapbackend.DTO;

import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private int id;
    private int rating;
    private String review;
    private LocalDateTime updated_at;
    private LocalDateTime created_at;
    private Integer account_id;
    private Integer course_id;
    private Integer test_id;
    private String fullname;
    private String image;

    public ReviewDTO(Integer id, LocalDateTime created_at, int rating, String review, LocalDateTime updated_at, Integer account_id, Integer course_id, String name, String image, Integer testId) {
        this.id = id;
        this.account_id = account_id;
        this.course_id = course_id;
        this.rating = rating;
        this.review = review;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.fullname = name;
        this.image = image;
        this.test_id = testId;
    }


}
