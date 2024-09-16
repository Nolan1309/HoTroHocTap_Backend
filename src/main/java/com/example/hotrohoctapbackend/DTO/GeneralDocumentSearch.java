package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class GeneralDocumentSearch {
    private int id;
    private LocalDateTime createdAt;
    private String description;
    private String image;
    private String title;
    private LocalDateTime updatedAt;
    private String url;
    private int view;
    private int id_category;
    private String categoryName;

    public GeneralDocumentSearch(int id, LocalDateTime createdAt, String description, String image, String title, LocalDateTime updatedAt, String url, int view,int id_category, String categoryName) {
        this.id = id;
        this.createdAt = createdAt;
        this.description = description;
        this.image = image;
        this.title = title;
        this.updatedAt = updatedAt;
        this.url = url;
        this.view = view;
        this.id_category = id_category;
        this.categoryName = categoryName;
    }


}
