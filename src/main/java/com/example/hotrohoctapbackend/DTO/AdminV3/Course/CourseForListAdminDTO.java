package com.example.hotrohoctapbackend.DTO.AdminV3.Course;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseForListAdminDTO {
    private int id;
    private String title;
    private String imageUrl;
    private String accountId;
    private String courseCategoryId;
    private BigDecimal cost;
    private BigDecimal price;
}
