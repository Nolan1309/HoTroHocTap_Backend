package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "amount")
    private int amount;

    @Column(name = "type_payments")
    private Boolean type_payments;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


}
