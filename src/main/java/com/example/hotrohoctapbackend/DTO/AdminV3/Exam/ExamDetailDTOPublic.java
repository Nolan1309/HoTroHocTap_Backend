package com.example.hotrohoctapbackend.DTO.AdminV3.Exam;

import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExamDetailDTOPublic {
    private Integer testId;
    private String title;
    private Integer courseId;
    private String courseTitle;

    private String author;
    private Integer itemCountReview;
    private Double rating;

    private Integer totalQuestion;
    private ExamLevel level;
    private ExamType examType;
    private String description;
    private String intro;
    private String testContent;
    private String knowledgeRequirement;
    private String imageUrl;
    private Integer duration;

    private Integer itemCountPrice;
    private ExamStatus status;
    private BigDecimal price;
    private BigDecimal cost;
    private Integer percentDiscount;
    private Boolean purchased;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
