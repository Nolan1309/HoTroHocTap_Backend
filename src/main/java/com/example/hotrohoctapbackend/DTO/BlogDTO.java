package com.example.hotrohoctapbackend.DTO;

import java.time.LocalDateTime;

public class BlogDTO {
    private Integer id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    private Integer author_id;
    private Integer cat_blog_id;
    private Boolean status;

    private String author_name;
    private String category_name;

    public BlogDTO(Integer id,  String content, LocalDateTime created_at,String title, LocalDateTime updated_at, Integer author_id,
                   Integer cat_blog_id, Boolean status, String image,String author_name, String category_name) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.author_id = author_id;
        this.cat_blog_id = cat_blog_id;
        this.status = status;
        this.image = image;
        this.author_name = author_name;
        this.category_name =category_name;
    }

    public BlogDTO() {
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getCat_blog_id() {
        return cat_blog_id;
    }

    public void setCat_blog_id(Integer cat_blog_id) {
        this.cat_blog_id = cat_blog_id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
