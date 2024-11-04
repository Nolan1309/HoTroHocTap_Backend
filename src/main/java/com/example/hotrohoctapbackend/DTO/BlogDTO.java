package com.example.hotrohoctapbackend.DTO;

public class BlogDTO {
    private Integer id;               // Thêm trường id
    private String title;
    private String authorFullName;
    private String category;
    private String status;

    // Constructors
    public BlogDTO() {}

    public BlogDTO(Integer id, String title, String authorFullName, String category, String status) {
        this.id = id;
        this.title = title;
        this.authorFullName = authorFullName;
        this.category = category;
        this.status = status;
    }

    // Getters and Setters
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

    public String getAuthorFullName() {
        return authorFullName;
    }

    public void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
