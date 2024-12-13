package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.List;

@Data
public class TestUserAnswerRequestDTO_User {

    private Integer accountId;
    private Integer courseId;
    private Integer totalQuestion;
    private Integer chapterId;
    private Integer lessonId;
    private boolean videoStatus;
    private boolean testStatus;
    private double testScore;
    private boolean isChapterTest;
    private List<UserAnswerDTO_User> userAnswers;

    public TestUserAnswerRequestDTO_User() {
    }

    public TestUserAnswerRequestDTO_User(Integer accountId, Integer courseId, Integer totalQuestion, Integer chapterId, Integer lessonId,
                                         boolean videoStatus, boolean testStatus, double testScore, boolean isChapterTest,
                                         List<UserAnswerDTO_User> userAnswers) {
        this.accountId = accountId;
        this.courseId = courseId;
        this.totalQuestion = totalQuestion;
        this.chapterId = chapterId;
        this.lessonId = lessonId;
        this.videoStatus = videoStatus;
        this.testStatus = testStatus;
        this.testScore = testScore;
        this.isChapterTest = isChapterTest;
        this.userAnswers = userAnswers;
    }
}



