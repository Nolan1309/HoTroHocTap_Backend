package com.example.hotrohoctapbackend.DTO;

import java.util.Date;
import java.util.List;

public class TestDTO {
    private int id;
    private String title;
    private String description;
    private boolean isSummary;
    private int totalQuestion;
    private Date createdAt;
    private Date updatedAt;

    private int lessonId;  // ID của Lesson
    private int chapterId; // ID của Chapter

    // Constructors
    public TestDTO() {
    }

    public TestDTO(int id, String title, String description, boolean isSummary, int totalQuestion, Date createdAt, Date updatedAt, int lessonId, int chapterId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isSummary = isSummary;
        this.totalQuestion = totalQuestion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lessonId = lessonId;
        this.chapterId = chapterId;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isSummary() {
        return isSummary;
    }

    public void setSummary(boolean isSummary) {
        this.isSummary = isSummary;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    // Nếu cần, bạn có thể thêm danh sách câu hỏi và các kết quả kiểm tra
    // Ví dụ:
    // private List<TestQuestionDTO> testQuestions;
    // private List<TestResultDTO> testResults;
}
