package com.example.hotrohoctapbackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "lesson_title")
    private String title;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private Chapter chapter;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;
    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;
    @Column(name = "isDeleted")
    private boolean isDeleted;

}