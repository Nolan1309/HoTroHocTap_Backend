package com.example.hotrohoctapbackend.DTO.AdminV3.Subscription;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MembershipPackageDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price; // Price can be a double or BigDecimal depending on your requirements
    private Integer duration; // Duration in months
    private List<String> features; // List of features included in the package
    //    private Integer discountPercentage;
    private String status; // 'ACTIVE' or 'INACTIVE'
    private Integer subscribersCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
