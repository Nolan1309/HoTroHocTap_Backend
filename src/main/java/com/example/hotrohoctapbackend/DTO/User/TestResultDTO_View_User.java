package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TestResultDTO_View_User {
    private Integer id;
    private Integer testId;
    private Integer accountId;
    private Integer courseId;
    private Double score;
    private Integer correctAnswers;
    private Integer incorrectAnswers;
    private Integer totalQuestions;
    private LocalDateTime completedAt;
    private String result;
    private LocalDateTime deletedDate;
    private Boolean deleted;
    private Boolean isChapterTest;
    private String testTitle;
}
