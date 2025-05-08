package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminTestAddDTO_V2 {
    @Getter
    private Integer id;
    @Getter
    private String title;
    @Getter
    private String description;
    @Getter
    private Integer lessonId;
    @Getter
    private Integer chapterId;
    @Getter
    private Integer courseId;
    @Getter
    private Integer totalQuestion;

    @Getter
    private Integer easyQuestion;

    @Getter
    private Integer mediumQuestion;

    @Getter
    private Integer hardQuestion;
    @Getter
    private List<String> type;

    private Boolean isSummary;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime updatedAt;

    private Integer duration;

    private String format;

    public AdminTestAddDTO_V2(Integer id, String title, String description, Integer lessonId, Integer chapterId, Integer courseId, Integer totalQuestion, Integer easyQuestion, Integer mediumQuestion, Integer hardQuestion, List<String> type, Boolean isSummary, LocalDateTime createdAt, LocalDateTime updatedAt, Integer duration, String format) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lessonId = lessonId;
        this.chapterId = chapterId;
        this.courseId = courseId;
        this.totalQuestion = totalQuestion;
        this.easyQuestion = easyQuestion;
        this.mediumQuestion = mediumQuestion;
        this.hardQuestion = hardQuestion;
        this.type = type;
        this.isSummary = isSummary;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.duration = duration;
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public AdminTestAddDTO_V2() {
    }

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

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(Integer totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getEasyQuestion() {
        return easyQuestion;
    }

    public void setEasyQuestion(Integer easyQuestion) {
        this.easyQuestion = easyQuestion;
    }

    public Integer getMediumQuestion() {
        return mediumQuestion;
    }

    public void setMediumQuestion(Integer mediumQuestion) {
        this.mediumQuestion = mediumQuestion;
    }

    public Integer getHardQuestion() {
        return hardQuestion;
    }

    public void setHardQuestion(Integer hardQuestion) {
        this.hardQuestion = hardQuestion;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Boolean getSummary() {
        return isSummary;
    }

    public void setSummary(Boolean summary) {
        isSummary = summary;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
