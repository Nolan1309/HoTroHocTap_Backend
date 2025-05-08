package com.example.hotrohoctapbackend.DTO.AdminV3.Evalution;

import lombok.Data;

@Data
public class ReviewUpdateRequest {
    private String status; // approved | pending | rejected
    private String review; // nội dung review chỉnh sửa
}