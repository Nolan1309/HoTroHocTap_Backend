package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "courses_title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_category_id")
    private CourseCategory courseCategory;

    @Column(name = "description",columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "LONGTEXT")
    private String image_url;

    @Column(name = "course_output",columnDefinition = "LONGTEXT")
    private String courseOutput;

    @Column(name = "language")
    private String language;

    @Column(name = "author")
    private String author;

    @Column(name = "duration")
    private String duration;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Boolean status;

    @Column(name="type")
    private String type;

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
