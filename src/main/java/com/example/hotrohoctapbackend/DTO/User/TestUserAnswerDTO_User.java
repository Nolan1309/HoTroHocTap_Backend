package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class TestUserAnswerDTO_User {  private Integer id;
    private Integer testId;
    private Integer questionId;
    private Integer accountId;
    private Integer courseId;
    private String result;
    private Integer testResultId;

    public TestUserAnswerDTO_User() {
    }

    public TestUserAnswerDTO_User(Integer id, Integer testId, Integer questionId, Integer accountId, Integer courseId, String result, Integer testResultId) {
        this.id = id;
        this.testId = testId;
        this.questionId = questionId;
        this.accountId = accountId;
        this.courseId = courseId;
        this.result = result;
        this.testResultId = testResultId;
    }
}
