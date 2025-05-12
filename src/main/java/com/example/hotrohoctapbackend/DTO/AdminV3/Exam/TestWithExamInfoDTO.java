package com.example.hotrohoctapbackend.DTO.AdminV3.Exam;

import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TestWithExamInfoDTO {
    // Từ Test
    private Integer testId;
    private String title;
    private String description;
    private Integer totalQuestion;
    private Integer easyQuestion;
    private Integer mediumQuestion;
    private Integer hardQuestion;
    private String type;
    private Integer courseId;
    private String courseTitle;

    private Integer point;
    private String format;
    private Integer duration;
    private Date createdAt;
    private Date updatedAt;
    private Integer itemCount;
    private Boolean discountStatus;

    // Từ ExamInfo
    private String intro;
    private String testContents;
    private String knowledgeRequirements;
    private String imageUrl;
    private ExamLevel level;
    private BigDecimal price;
    private BigDecimal cost;
    private ExamType examType;
    private ExamStatus status;
}
