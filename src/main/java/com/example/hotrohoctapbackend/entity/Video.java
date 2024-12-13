package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @Column(name = "videoTitle")
    private String title;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;
    @Column(name = "documentShort", columnDefinition = "TEXT")
    private String documentShort;
    @Column(name = "documentUrl", columnDefinition = "TEXT")
    private String documentUrl;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @Column(name = "isviewtest")
    private Boolean isViewTest = false; // Đặt mặc định là false


    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
        if (isViewTest == null) {
            isViewTest = false; // Đặt giá trị mặc định là false nếu chưa được gán
        }
    }
}