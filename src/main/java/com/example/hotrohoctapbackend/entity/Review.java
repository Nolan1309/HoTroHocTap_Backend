package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.ReviewStatus;
import com.example.hotrohoctapbackend.enums.ReviewType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "rating")
    private int rating;

    @Column(name = "review_type")
    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @Column(name = "review")
    private String review;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReviewStatus status = ReviewStatus.PENDING;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "created_at")
    private LocalDateTime created_at;

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
