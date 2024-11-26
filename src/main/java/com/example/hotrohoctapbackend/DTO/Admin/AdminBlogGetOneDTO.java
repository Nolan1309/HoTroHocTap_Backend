package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

@Data
public class AdminBlogGetOneDTO {
    private String title;
    private String content;
    private String image;
    private Boolean status;
    private Integer catBlogId;

    // Constructor
    public AdminBlogGetOneDTO(String title, String content, String image, Boolean status, Integer catBlogId) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.status = status;
        this.catBlogId = catBlogId;
    }
}
