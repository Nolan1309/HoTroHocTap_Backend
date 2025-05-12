package com.example.hotrohoctapbackend.DTO.AdminV3.Bundle;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoursePackageDTO {
    private Integer id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private String author;
}
