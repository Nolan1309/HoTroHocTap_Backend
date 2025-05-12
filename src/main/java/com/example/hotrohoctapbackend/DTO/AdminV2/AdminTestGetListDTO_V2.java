package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Getter;

import java.util.Date;
import java.util.List;

public class AdminTestGetListDTO_V2 {
    private Integer id;
    private String title;
    private int totalQuestion;

    private Integer easyQuestion;


    private Integer mediumQuestion;


    private Integer hardQuestion;

    private List<String> type;
    private Date createdAt;
    private boolean deleted;
    private Boolean summary;
    private Integer lessonId;
    private Integer chapterId;
    private Integer courseId;
    private String description;

    private Boolean isAssigned;


    @Getter

    private Integer duration;

    @Getter

    private String format;


    @Getter
    private Integer point;

    public void setFormat(String format) {
        this.format = format;
    }

    public AdminTestGetListDTO_V2() {
    }

    public AdminTestGetListDTO_V2(Integer id, String title, int totalQuestion, Integer easyQuestion, Integer mediumQuestion, Integer hardQuestion, List<String> type, Date createdAt, boolean deleted, Boolean summary, Integer lessonId, Integer chapterId, Integer courseId, String description, Boolean isAssigned, Integer duration, String format, Integer point) {
        this.id = id;
        this.title = title;
        this.totalQuestion = totalQuestion;
        this.easyQuestion = easyQuestion;
        this.mediumQuestion = mediumQuestion;
        this.hardQuestion = hardQuestion;
        this.type = type;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.summary = summary;
        this.lessonId = lessonId;
        this.chapterId = chapterId;
        this.courseId = courseId;
        this.description = description;
        this.isAssigned = isAssigned;
        this.duration = duration;
        this.format = format;
        this.point = point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }


    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getAssigned() {
        return isAssigned;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

