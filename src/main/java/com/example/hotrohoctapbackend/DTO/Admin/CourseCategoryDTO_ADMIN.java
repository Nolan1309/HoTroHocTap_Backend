package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

@Data
public class CourseCategoryDTO_ADMIN {
    private Integer categoryId;
    private String categoryName;

    public CourseCategoryDTO_ADMIN(Integer categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public CourseCategoryDTO_ADMIN() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
