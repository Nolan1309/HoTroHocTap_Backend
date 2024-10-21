package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class ProgressDTO_User {
    private int accountId;
    private int courseId;
    private int chapterId;
    private int lessonId;
    private boolean videoStatus;
    private boolean testStatus;
    private Integer testScore;

    public ProgressDTO_User(int accountId, int courseId, int chapterId, int lessonId, boolean videoStatus, boolean testStatus, Integer testScore) {
        this.accountId = accountId;
        this.courseId = courseId;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
        this.videoStatus = videoStatus;
        this.testStatus = testStatus;
        this.testScore = testScore;
    }
}
