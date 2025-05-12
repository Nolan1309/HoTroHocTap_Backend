package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "payment_date")
    private LocalDateTime payment_date;

    @Column(name = "total_payment", precision = 18, scale = 2)
    private BigDecimal total_payment;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    public enum PaymentType {
        COURSE,
        VIP
    }
}
