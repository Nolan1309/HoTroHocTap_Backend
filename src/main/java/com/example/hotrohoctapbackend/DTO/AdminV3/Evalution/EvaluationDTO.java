package com.example.hotrohoctapbackend.DTO.AdminV3.Evalution;

import lombok.Data;

@Data
public class EvaluationDTO {
    private Integer id;
    private Integer courseId;
    private String courseName;
    private Integer testId;
    private String testName;
    private Integer accountId;
    private String accountName;
    private int rating;
    private String review;
    private String createdAt;
    private String updatedAt;
    private String deletedDate;
    private boolean isDeleted;
    private String reviewType; // "COURSE" or "TEST"
    private String status;     // "approved", "pending", "rejected"
}