package com.example.hotrohoctapbackend.DTO.AdminV3.Subscription;

import lombok.Data;

import java.util.List;

@Data
public class CreateMembershipPackageDTO {
    private String name;
    private String description;
    private Double price;  // Giá gói thành viên
    private Integer duration;  // Thời gian tính bằng tháng
    private List<String> features;  // Danh sách tính năng
    private Integer discountPercentage;  // % giảm giá
    private String status;
}
