package com.example.hotrohoctapbackend.entity;

import com.google.type.Decimal;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; // Mã ví

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "balance_wallet")
    private Decimal balance;

    @Column(name = "currency_wallet")
    private String currency; // Loại tiền tệ

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Boolean status;
}
