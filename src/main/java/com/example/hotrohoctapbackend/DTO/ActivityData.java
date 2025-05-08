package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class ActivityData {
    private String activityType;  // 'login', 'video_clicked', 'test_clicked'
    private int accountId;
    private Integer videoId;      // Nếu là video
    private Integer testId;       // Nếu là bài kiểm tra
    private String timestamp;

    public ActivityData() {
    }

    public ActivityData(String activityType, int accountId, Integer videoId, Integer testId, String timestamp) {
        this.activityType = activityType;
        this.accountId = accountId;
        this.videoId = videoId;
        this.testId = testId;
        this.timestamp = timestamp;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
