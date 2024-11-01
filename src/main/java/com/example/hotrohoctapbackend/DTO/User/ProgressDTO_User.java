package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ProgressDTO_User {
    private int accountId;
    private int courseId;
    private int chapterId;
    private Integer lessonId;
    private boolean videoStatus;
    private boolean testStatus;
    private Double testScore;
    private boolean isChapterTest;

    public ProgressDTO_User(int accountId, int courseId, int chapterId, Integer lessonId, boolean videoStatus, boolean testStatus, Double testScore) {
        this.accountId = accountId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
        this.videoStatus = videoStatus;
        this.testStatus = testStatus;
        this.testScore = testScore;
    }

    public ProgressDTO_User(int accountId, int courseId, int chapterId, Integer lessonId, boolean videoStatus, boolean testStatus, Double testScore, boolean isChapterTest) {
        this.accountId = accountId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
        this.videoStatus = videoStatus;
        this.testStatus = testStatus;
        this.testScore = testScore;
        this.isChapterTest = isChapterTest;
    }
}
