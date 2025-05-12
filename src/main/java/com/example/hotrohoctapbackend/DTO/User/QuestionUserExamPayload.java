package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.List;

@Data
public class QuestionUserExamPayload {
    private List<QuestionUserExamPayloadDTO> userAnswers;
    private Integer testId;
    private Integer accountId;
    private Integer courseId;

    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public List<QuestionUserExamPayloadDTO> getUserAnswers() {
        return userAnswers;
    }

    public QuestionUserExamPayload(List<QuestionUserExamPayloadDTO> userAnswers, Integer testId, Integer accountId, Integer courseId) {
        this.userAnswers = userAnswers;
        this.testId = testId;
        this.accountId = accountId;
        this.courseId = courseId;
    }

    public QuestionUserExamPayload() {
    }

    public void setUserAnswers(List<QuestionUserExamPayloadDTO> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public QuestionUserExamPayload(List<QuestionUserExamPayloadDTO> userAnswers) {
        this.userAnswers = userAnswers;
    }
}
