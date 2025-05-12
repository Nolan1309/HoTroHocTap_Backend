package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserGetCheckProgress {
    private int id;
    private boolean videoCompleted;
    private boolean testCompleted;
    private boolean chapterTested;
    private Double testScore;
    private LocalDateTime completedAt;
    private int accountId;  // Chỉ chứa ID của account, có thể bạn sẽ cần trả về thêm thông tin khác.
    private int courseId;   // Chỉ chứa ID của course
    private int chapterId;  // Chỉ chứa ID của chapter
    private int lessonId;

    public UserGetCheckProgress() {
    }

    public UserGetCheckProgress(int id, boolean videoCompleted, boolean testCompleted, boolean chapterTested, Double testScore, LocalDateTime completedAt, int accountId, int courseId, int chapterId, int lessonId) {
        this.id = id;
        this.videoCompleted = videoCompleted;
        this.testCompleted = testCompleted;
        this.chapterTested = chapterTested;
        this.testScore = testScore;
        this.completedAt = completedAt;
        this.accountId = accountId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
    }
}
