package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProgressDTO {
    private Integer id;
    private Boolean isChapterTest;
    private LocalDateTime completedAt;
    private Boolean testCompleted;
    private Double testScore;
    private Boolean videoCompleted;
    private Integer accountId;
    private Integer chapterId;
    private Integer courseId;
    private Integer lessonId;
}
