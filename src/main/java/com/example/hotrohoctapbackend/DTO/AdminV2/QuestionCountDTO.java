package com.example.hotrohoctapbackend.DTO.AdminV2;

public class QuestionCountDTO {
    private String questionType;  // Thêm thuộc tính loại câu hỏi
    private Long totalQuestions;
    private Long easyQuestions;
    private Long mediumQuestions;
    private Long hardQuestions;

    // Constructor mặc định
    public QuestionCountDTO() {
    }

    // Constructor có tham số
    public QuestionCountDTO(String questionType, Long totalQuestions, Long easyQuestions, Long mediumQuestions, Long hardQuestions) {
        this.questionType = questionType;
        this.totalQuestions = totalQuestions;
        this.easyQuestions = easyQuestions;
        this.mediumQuestions = mediumQuestions;
        this.hardQuestions = hardQuestions;
    }

    // Getter và Setter
    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getEasyQuestions() {
        return easyQuestions;
    }

    public void setEasyQuestions(Long easyQuestions) {
        this.easyQuestions = easyQuestions;
    }

    public Long getMediumQuestions() {
        return mediumQuestions;
    }

    public void setMediumQuestions(Long mediumQuestions) {
        this.mediumQuestions = mediumQuestions;
    }

    public Long getHardQuestions() {
        return hardQuestions;
    }

    public void setHardQuestions(Long hardQuestions) {
        this.hardQuestions = hardQuestions;
    }
}
