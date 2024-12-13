package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
@Data
public class QuestionDownloadDTO {
    private Integer id;
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String userAnswer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestionDownloadDTO(Integer id, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String userAnswer) {
        this.id = id;
        this.question = question;
        this.options = Arrays.asList(optionA, optionB, optionC, optionD);
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
