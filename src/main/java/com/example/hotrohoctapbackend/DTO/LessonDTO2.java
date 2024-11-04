package com.example.hotrohoctapbackend.DTO;

import java.time.LocalDateTime;

public class LessonDTO2 {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int duration;  // Đơn vị tính là phút
    private int chapter_id;

    // Constructors
    public LessonDTO2() {
    }

    public LessonDTO2(int id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, int duration, int chapter_id) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.duration = duration;
        this.chapter_id = chapter_id;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }
}
