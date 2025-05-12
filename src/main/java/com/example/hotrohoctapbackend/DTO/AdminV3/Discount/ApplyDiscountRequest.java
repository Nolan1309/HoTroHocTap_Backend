package com.example.hotrohoctapbackend.DTO.AdminV3.Discount;

import lombok.Data;

import java.util.List;

@Data
public class ApplyDiscountRequest {
    private String voucherType;     // Loại giảm giá (COURSE hoặc TEST hoặc COMBO)
    private List<Integer> targetIds; // Danh sách ID các mục tiêu (testId hoặc courseId)
    private int discountId;
}
