package com.example.hotrohoctapbackend.DTO.AdminV3.Test;

import lombok.Data;

import java.util.List;

@Data
public class TestDTO {
    private String id;
    private String title;
    private String type;
    private Integer lessonId;
    private Integer chapterId;
    private Integer courseId;
    private String description;
    private Boolean summary;
    private Integer totalQuestion;
    private Integer easyQuestion;
    private Integer mediumQuestion;
    private Integer hardQuestion;
    private String createdAt;
    private String updatedAt;
    private String deletedDate;
    private Boolean deleted;
    private Integer duration;
    private String format;
    private Boolean assigned;
    private Integer point;

    public TestDTO() {
    }

    public TestDTO(String id, String title, String type, Integer lessonId, Integer chapterId, Integer courseId, String description, Boolean summary, Integer totalQuestion, Integer easyQuestion, Integer mediumQuestion, Integer hardQuestion, String createdAt, String updatedAt, String deletedDate, Boolean deleted, Integer duration, String format, Boolean assigned, Integer point) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.lessonId = lessonId;
        this.chapterId = chapterId;
        this.courseId = courseId;
        this.description = description;
        this.summary = summary;
        this.totalQuestion = totalQuestion;
        this.easyQuestion = easyQuestion;
        this.mediumQuestion = mediumQuestion;
        this.hardQuestion = hardQuestion;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedDate = deletedDate;
        this.deleted = deleted;
        this.duration = duration;
        this.format = format;
        this.assigned = assigned;
        this.point = point;
    }
}
