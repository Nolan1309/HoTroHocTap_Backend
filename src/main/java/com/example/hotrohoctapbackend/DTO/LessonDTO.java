package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class LessonDTO {
    private int id;
    private String title;
    private Boolean deleted;

    // Constructors
    public LessonDTO() {
    }

    public LessonDTO(int id, String title, Boolean deleted) {
        this.id = id;
        this.title = title;
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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
