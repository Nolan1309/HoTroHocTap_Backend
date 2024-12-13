package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.util.Date;

@Data
public class AdminTestGetDTO_Version2 {
    private Integer id;
    private String title;
    private int totalQuestion;
    private Date createdAt;
    private boolean deleted;
    private Boolean summary;
    private Integer lessonId;
    private Integer courseId;

    public AdminTestGetDTO_Version2(Integer id, String title, int totalQuestion, Date createdAt, boolean deleted, Boolean summary, Integer lessonId, Integer courseId) {
        this.id = id;
        this.title = title;
        this.totalQuestion = totalQuestion;
        this.createdAt = createdAt;
        this.deleted = deleted;
        this.summary = summary;
        this.lessonId = lessonId;
        this.courseId = courseId;
    }
}
