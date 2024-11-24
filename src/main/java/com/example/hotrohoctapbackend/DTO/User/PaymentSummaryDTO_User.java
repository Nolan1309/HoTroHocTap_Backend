package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentSummaryDTO_User {
    private Integer paymentId;
    private LocalDateTime paymentDate;
    private BigDecimal totalPayment;
    private Long courseCount;
    private String paymentMethod;
}
