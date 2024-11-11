package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.Date;

@Data
public class TestDTO_User {
    private int id;
    private String title;
    private String description;
    private boolean isSummary;
    private int totalQuestion;
    private Date createdAt;
    private Date updatedAt;
    private int lesson_id;
    private int chapter_id;
    private int course_id;


}
