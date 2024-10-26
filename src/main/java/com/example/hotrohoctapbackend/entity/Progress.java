package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "video_completed", columnDefinition = "BIT", nullable = false)
    private boolean videoCompleted = false;

    @Column(name = "test_completed", columnDefinition = "BIT", nullable = false)
    private boolean testCompleted = false;

    @Column(name = "is_chapter_test", columnDefinition = "BIT", nullable = false)
    private boolean chapterTested = false;

    @Column(name = "test_score")
    private Integer testScore;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapterId")
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lessonId")
    private Lesson lesson;

}
