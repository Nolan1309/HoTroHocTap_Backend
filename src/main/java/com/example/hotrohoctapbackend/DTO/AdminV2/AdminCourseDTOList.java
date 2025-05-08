package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminCourseDTOList {
    @Getter
    private int id;
    @Getter
    private String title;
    @Getter
    private String description;
    @Getter
    private String imageUrl;
    @Getter
    private String courseOutput;
    @Getter
    private String language;
    @Getter
    private String author;
    @Getter
    private Integer duration;
    @Getter
    private BigDecimal cost;
    @Getter
    private BigDecimal price;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime updatedAt;
    @Getter
    private Boolean status;
    @Getter
    private String type;
    @Getter
    private LocalDateTime deletedDate;
    private boolean isDeleted;
    @Getter
    private Integer accountId;
    @Getter
    private String categoryNameLevel3;
    @Getter
    private Integer categoryIdLevel3;

    // Thông tin danh mục cấp 2
    @Getter
    private String categoryNameLevel2;
    @Getter
    private Integer categoryIdLevel2;

    // Thông tin danh mục cấp 1
    @Getter
    private String categoryNameLevel1;
    @Getter
    private Integer categoryIdLevel1;
    @Getter
    private String level;

    @Getter
    private Long countStudent;

    public AdminCourseDTOList(int id, String title, String description, String imageUrl, String courseOutput, String language,
                              String author, Integer duration, BigDecimal cost, BigDecimal price, LocalDateTime createdAt,
                              LocalDateTime updatedAt, Boolean status, String type, LocalDateTime deletedDate, boolean isDeleted, Integer accountId, String categoryNameLevel3, Integer categoryIdLevel3,
                              String categoryNameLevel2, Integer categoryIdLevel2, String categoryNameLevel1, Integer categoryIdLevel1, Long countStudent, String level) {
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
        this.countStudent = countStudent;
        this.level = level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCourseOutput(String courseOutput) {
        this.courseOutput = courseOutput;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
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

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setCategoryNameLevel3(String categoryNameLevel3) {
        this.categoryNameLevel3 = categoryNameLevel3;
    }

    public void setCategoryIdLevel3(Integer categoryIdLevel3) {
        this.categoryIdLevel3 = categoryIdLevel3;
    }

    public void setCategoryNameLevel2(String categoryNameLevel2) {
        this.categoryNameLevel2 = categoryNameLevel2;
    }

    public void setCategoryIdLevel2(Integer categoryIdLevel2) {
        this.categoryIdLevel2 = categoryIdLevel2;
    }

    public void setCategoryNameLevel1(String categoryNameLevel1) {
        this.categoryNameLevel1 = categoryNameLevel1;
    }

    public void setCategoryIdLevel1(Integer categoryIdLevel1) {
        this.categoryIdLevel1 = categoryIdLevel1;
    }

    public void setCountStudent(Long countStudent) {
        this.countStudent = countStudent;
    }
}
