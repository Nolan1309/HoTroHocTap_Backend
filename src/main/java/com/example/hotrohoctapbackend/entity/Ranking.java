package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "rankings")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "overall_score")
    private int overall_score;

    @Column(name = "total_score")
    private int total_score;

    @Column(name = "ranking")
    private int ranking;
    @Column(name = "created_at")
    private LocalDateTime create_at;

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
