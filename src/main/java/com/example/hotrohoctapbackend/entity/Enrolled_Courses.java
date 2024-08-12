package com.example.hotrohoctapbackend.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrolled_courses")
@Data
public class Enrolled_Courses {
    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Course course;

    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;

    @Column(name = "status")
    private String status;
}