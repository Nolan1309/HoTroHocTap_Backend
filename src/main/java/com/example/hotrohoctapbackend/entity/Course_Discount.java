package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.DiscountType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "course_discounts")
public class Course_Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = true)
    private Course course;
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = true)
    private Discount discount;
    @ManyToOne
    @JoinColumn(name = "test_id", nullable = true)
    private Test test;

    @Enumerated(EnumType.STRING)
    @Column(name = "discountType")
    private DiscountType discountType;

    @Column(name = "status")
    private boolean status = false;

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
