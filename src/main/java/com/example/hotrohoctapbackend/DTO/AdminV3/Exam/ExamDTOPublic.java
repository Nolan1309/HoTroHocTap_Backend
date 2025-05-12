package com.example.hotrohoctapbackend.DTO.AdminV3.Exam;

import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExamDTOPublic {
    private Integer testId;
    private String title;
    private String description;
    private Integer totalQuestion;
    private Integer courseId;
    private String courseTitle;
    private String author;
    private Integer itemCountPrice;
    private Integer itemCountReview;
    private Double rating;
    private String imageUrl;
    private Integer duration;
    private ExamLevel level;
    private ExamType examType;
    private ExamStatus status;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer percentDiscount;
    private Boolean purchased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
