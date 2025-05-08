package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "point_history")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")// Tự động tăng
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Mối quan hệ với người dùng (account)

    @Column(name = "points")
    private int points; // Số điểm cộng

    @Column(name = "reason")
    private String reason; // Lý do cộng điểm (ví dụ: mua hàng, tham gia sự kiện)


    @Column(name = "status")
    private String status; // Trạng thái giao dịch (pending, completed, expired)

    @Column(name = "createdAt")
    private LocalDateTime createdAt; // Thời gian cộng điểm

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt; // Thời gian cập nhật

    @Column(name = "type")
    private String type;
}
