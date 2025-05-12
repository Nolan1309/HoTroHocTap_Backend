package com.example.hotrohoctapbackend.DTO.AdminV3.Blog;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class BlogDTOPublic {
    private String id;
    private String title;
    private String content;
    private String summary;
    private String author_id;
    private String createdAt;
    private String updatedAt;
    private Boolean status;
    private Boolean featured;
    private String cat_blog_id;
    private String image;

    private Integer views;
    private Integer commentCount;
    private boolean isDeleted;
    private String deletedDate;
    private String categoryName;
    private String authorName;
}
