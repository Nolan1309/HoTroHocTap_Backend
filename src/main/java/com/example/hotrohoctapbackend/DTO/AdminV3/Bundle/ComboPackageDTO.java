package com.example.hotrohoctapbackend.DTO.AdminV3.Bundle;

import com.example.hotrohoctapbackend.entity.CourseBundleItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComboPackageDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price; // Giá đã giảm
    private String imageUrl;
    private Double originalPrice; // Giá gốc

    private List<CoursePackageDTO> courses; // Danh sách khóa học trong combo
    private Integer discount; // Phần trăm giảm giá

    private String status; // 'ACTIVE' hoặc 'INACTIVE'
    private Integer salesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
