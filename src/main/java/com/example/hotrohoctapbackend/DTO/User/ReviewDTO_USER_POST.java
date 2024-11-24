package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO_USER_POST {
    private int rating;
    private String review;
    private int account_id;
    private int course_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
