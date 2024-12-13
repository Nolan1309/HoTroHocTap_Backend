package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminBlogDTO {
    private Integer id;
    private String title;
    private String fullname;
    private String categoryName;
    private Boolean status;
    private Boolean isDeleted;

    // Constructor
    public AdminBlogDTO(Integer id, String title, String fullname, String categoryName, Boolean status, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.fullname = fullname;
        this.categoryName = categoryName;
        this.status = status;
        this.isDeleted = isDeleted;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "AdminBlogDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", fullname='" + fullname + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", status=" + status +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
