package com.example.hotrohoctapbackend.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BlogCategoryDTO {

    private int id;

    private String name;

    private String description;

    private Timestamp createdAt; // Đổi từ LocalDateTime sang Timestamp
    private Timestamp updatedAt;

    private int totalblog;

    public BlogCategoryDTO(int id, String name, String description, Timestamp createdAt, Timestamp updatedAt, int totalblog) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.totalblog = totalblog;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalblog() {
        return totalblog;
    }

    public void setTotalblog(int totalblog) {
        this.totalblog = totalblog;
    }
}
