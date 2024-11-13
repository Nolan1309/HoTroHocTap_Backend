package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_type")
    private String discount_type;

    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discount_value;

    @Column(name = "start_date")
    private LocalDateTime start_date;

    @Column(name = "end_date")
    private LocalDateTime end_date;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }
}
