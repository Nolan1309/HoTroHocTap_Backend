package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminCourseResultDTO_V2 {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private String courseOutput;
    private String language;
    private String author;
    private Integer duration;
    private BigDecimal cost;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean status;
    private String type;
    private LocalDateTime deletedDate;
    private boolean isDeleted;
    private Integer accountId;
    private String categoryNameLevel3;
    private Integer categoryIdLevel3;

    // Thông tin danh mục cấp 2
    private String categoryNameLevel2;
    private Integer categoryIdLevel2;

    // Thông tin danh mục cấp 1
    private String categoryNameLevel1;
    private Integer categoryIdLevel1;

    public AdminCourseResultDTO_V2(int id, String title, String description, String imageUrl, String courseOutput, String language, String author, Integer duration, BigDecimal cost, BigDecimal price, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean status, String type, LocalDateTime deletedDate, boolean isDeleted, Integer accountId, String categoryNameLevel3, Integer categoryIdLevel3, String categoryNameLevel2, Integer categoryIdLevel2, String categoryNameLevel1, Integer categoryIdLevel1) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.courseOutput = courseOutput;
        this.language = language;
        this.author = author;
        this.duration = duration;
        this.cost = cost;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.type = type;
        this.deletedDate = deletedDate;
        this.isDeleted = isDeleted;
        this.accountId = accountId;
        this.categoryNameLevel3 = categoryNameLevel3;
        this.categoryIdLevel3 = categoryIdLevel3;
        this.categoryNameLevel2 = categoryNameLevel2;
        this.categoryIdLevel2 = categoryIdLevel2;
        this.categoryNameLevel1 = categoryNameLevel1;
        this.categoryIdLevel1 = categoryIdLevel1;
    }

    public String getCategoryNameLevel3() {
        return categoryNameLevel3;
    }

    public void setCategoryNameLevel3(String categoryNameLevel3) {
        this.categoryNameLevel3 = categoryNameLevel3;
    }

    public Integer getCategoryIdLevel3() {
        return categoryIdLevel3;
    }

    public void setCategoryIdLevel3(Integer categoryIdLevel3) {
        this.categoryIdLevel3 = categoryIdLevel3;
    }

    public String getCategoryNameLevel2() {
        return categoryNameLevel2;
    }

    public void setCategoryNameLevel2(String categoryNameLevel2) {
        this.categoryNameLevel2 = categoryNameLevel2;
    }

    public Integer getCategoryIdLevel2() {
        return categoryIdLevel2;
    }

    public void setCategoryIdLevel2(Integer categoryIdLevel2) {
        this.categoryIdLevel2 = categoryIdLevel2;
    }

    public String getCategoryNameLevel1() {
        return categoryNameLevel1;
    }

    public void setCategoryNameLevel1(String categoryNameLevel1) {
        this.categoryNameLevel1 = categoryNameLevel1;
    }

    public Integer getCategoryIdLevel1() {
        return categoryIdLevel1;
    }

    public void setCategoryIdLevel1(Integer categoryIdLevel1) {
        this.categoryIdLevel1 = categoryIdLevel1;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCourseOutput() {
        return courseOutput;
    }

    public void setCourseOutput(String courseOutput) {
        this.courseOutput = courseOutput;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}
