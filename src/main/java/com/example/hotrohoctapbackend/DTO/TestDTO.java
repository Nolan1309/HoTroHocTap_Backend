package com.example.hotrohoctapbackend.DTO;

import java.util.Date;
import java.util.List;

public class TestDTO {
    private Integer id;
    private String title;
    private String description;
    private boolean isSummary;
    private Integer totalQuestion;
    private Date createdAt;
    private Date updatedAt;

    private Integer lessonId;  // ID của Lesson
    private Integer chapterId; // ID của Chapter

    // Constructors
    public TestDTO() {
    }

    public TestDTO(Integer id, String title, String description, boolean isSummary, Integer totalQuestion, Date createdAt, Date updatedAt, Integer lessonId, Integer chapterId) {
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
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
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

    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    // Nếu cần, bạn có thể thêm danh sách câu hỏi và các kết quả kiểm tra
    // Ví dụ:
    // private List<TestQuestionDTO> testQuestions;
    // private List<TestResultDTO> testResults;
}
