package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class LessonDTO {
    private int id;
    private String title;

    // Constructors
    public LessonDTO() {
    }

    public LessonDTO(int id, String title) {
        this.id = id;
        this.title = title;
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
}
