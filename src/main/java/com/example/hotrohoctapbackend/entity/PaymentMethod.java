package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "method_name")
    private String method_name; // Tên phương thức thanh toán (ví dụ: "Ví điện tử", "Thẻ ngân hàng")

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet; // Mối quan hệ với ví của người dùng
}
