package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_info")
@Data
public class ExamInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "test_id", unique = true, nullable = false)
    private Test test;

    @Column(name = "intro", columnDefinition = "TEXT")
    private String intro;

    @Column(name = "test_contents", columnDefinition = "TEXT")
    private String testContents;

    @Column(name = "knowledge_requirements", columnDefinition = "TEXT")
    private String knowledgeRequirements;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private ExamLevel level;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "cost")
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "examType")
    private ExamType examType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
