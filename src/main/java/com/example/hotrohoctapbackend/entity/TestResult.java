package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "test_results")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_result_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "score")
    private double score;

    @Column(name = "correct_answers")
    private int correct_answers;

    @Column(name = "incorrect_answers")
    private int incorrect_answers;

    @Column(name = "total_questions")
    private int total_questions;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "result")
    private String result;

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
