package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestResultDTO_User {
    private Integer id;
    private Integer testID;
    private Integer accountID;
    private Integer courseID;
    private double score;
    private int correct_answers;
    private int incorrect_answers;
    private int total_questions;
    private LocalDateTime completedAt;
    private String result;
    private boolean isChapterTest;

    public TestResultDTO_User() {
    }

    public TestResultDTO_User(Integer id, Integer testID, Integer accountID,
                              Integer courseID, double score, int correct_answers,
                              int incorrect_answers, int total_questions, LocalDateTime completedAt,
                              String result) {
        this.id = id;
        this.testID = testID;
        this.accountID = accountID;
        this.courseID = courseID;
        this.score = score;
        this.correct_answers = correct_answers;
        this.incorrect_answers = incorrect_answers;
        this.total_questions = total_questions;
        this.completedAt = completedAt;
        this.result = result;
    }
}
