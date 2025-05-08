package com.example.hotrohoctapbackend.DTO.AdminV3.Discount;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountItemDTO {
    private String id;
    private String code;
    private String title;
    private String discountType;
    private String voucherType;
    private String description;
    private String value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer minOrderValue;
    private Integer maxUsed;
    private Integer usedCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Boolean isDeleted;

}
