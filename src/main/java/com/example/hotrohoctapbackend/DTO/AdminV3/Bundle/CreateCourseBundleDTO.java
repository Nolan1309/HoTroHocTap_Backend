package com.example.hotrohoctapbackend.DTO.AdminV3.Bundle;

import lombok.Data;

import java.util.List;

@Data
public class CreateCourseBundleDTO {
    private String name;  // Tên gói combo
    private String description;  // Mô tả ngắn
    private Double price;  // Giá combo
    private Double originalPrice;  // Giá gốc của combo
    private List<Integer> courseIds;  // Danh sách ID các khóa học
    private Integer discount;  // Phần trăm giảm giá
    private String status;
}
