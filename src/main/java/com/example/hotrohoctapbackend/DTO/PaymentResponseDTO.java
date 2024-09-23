package com.example.hotrohoctapbackend.DTO;

import com.google.type.Decimal;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private int id;
    private LocalDateTime payment_date;
    private BigDecimal total_payment;
    private String paymentMethod;
    private int account_id;
}
