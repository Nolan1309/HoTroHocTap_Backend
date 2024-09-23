package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PaymentDetailDTO {
    private Integer id;
    private BigDecimal price;
    private String courseTitle;
    private Integer paymentId;
    private Integer courseId;
}
