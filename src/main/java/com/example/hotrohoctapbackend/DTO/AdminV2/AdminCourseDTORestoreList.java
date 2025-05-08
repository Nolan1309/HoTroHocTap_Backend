package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminCourseDTORestoreList {
    private Integer id;
    private String author;
    private BigDecimal cost;
    private String courseOutput;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String description;
    private Integer duration;
    private String imageUrl;
    private Boolean isDeleted;
    private String language;
    private BigDecimal price;
    private Boolean status;
    private String coursesTitle;
    private String type;
    private LocalDateTime updatedAt;
    private Integer courseCategoryId;
    private Integer accountId;
}
