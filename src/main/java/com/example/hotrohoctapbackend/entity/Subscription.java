package com.example.hotrohoctapbackend.entity;

import com.google.type.Decimal;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription")
@Data
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "price")
    private Decimal price;

    @Column(name = "name")
    private String name;

    @Column(name = "duration_days")
    private Integer duration_days;

    @Column(name = "description")
    private String description;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "created_at")
    private LocalDateTime created_at;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }
}
