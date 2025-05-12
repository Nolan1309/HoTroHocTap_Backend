package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class TestWithQuestionCountDTO {
    private Integer testId;
    private String title;
    private String description;
    private Integer duration;
    private String testType;
    private Long totalQuestions;

    public TestWithQuestionCountDTO() {
    }

    public TestWithQuestionCountDTO(Integer testId, String title, String description, Integer duration, String testType, Long totalQuestions) {
        this.testId = testId;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.testType = testType;
        this.totalQuestions = totalQuestions;
    }

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
