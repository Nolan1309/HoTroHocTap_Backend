package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "enrolled_courses")
public class Enrolled_Courses {
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
    @JoinColumn(name = "bundle_id", nullable = true)
    private CourseBundle bundle;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "status")
    private String status;

    // ===== THÔNG TIN CHỨNG CHỈ =====
    @Column(name = "certificate_code", unique = true)
    private String certificateCode;

    @Column(name = "certificate_url")
    private String certificateUrl;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "certificate_verified")
    private Boolean certificateVerified;
}