package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "learning_results")
@Data
public class LearningResult {
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

    @Column(name = "overall_score")
    private int overall_score;

    @Column(name = "state")
    private String state;

    @Column(name = "learning_mode")
    private String learning_mode;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
