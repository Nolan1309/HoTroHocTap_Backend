package com.example.hotrohoctapbackend.DTO.AdminV3.Course;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDTOAdminV3 {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private String language;
    private String author;
    private String courseOutput;
    private BigDecimal cost;
    private BigDecimal price;
    private int duration;
    private String type;
    private boolean status;

    private String createdAt;
    private String updatedAt;
    private String deletedDate;
    private Boolean deleted;

    private String accountId;
    private String courseCategoryId;

    private String categoryName;
    private Integer studentCount;
    private Double rating;
    private String level;
    private String certificate;
    private Boolean purchased;


    public CourseDTOAdminV3() {
    }

    public CourseDTOAdminV3(int id, String title, String description, String imageUrl, String language, String author, String courseOutput, BigDecimal cost, BigDecimal price, int duration, String type, boolean status, String createdAt, String updatedAt, String deletedDate, Boolean deleted, String accountId, String courseCategoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.language = language;
        this.author = author;
        this.courseOutput = courseOutput;
        this.cost = cost;
        this.price = price;
        this.duration = duration;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedDate = deletedDate;
        this.deleted = deleted;
        this.accountId = accountId;
        this.courseCategoryId = courseCategoryId;
    }

    public CourseDTOAdminV3(int id, String title, String description, String imageUrl, String language, String author, String courseOutput,
                            BigDecimal cost, BigDecimal price, int duration, String type, boolean status, String createdAt, String updatedAt,
                            String deletedDate, Boolean deleted, String accountId, String courseCategoryId, String categoryName,
                            Integer studentCount, Double rating, String level, String certificate, Boolean purchased) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.language = language;
        this.author = author;
        this.courseOutput = courseOutput;
        this.cost = cost;
        this.price = price;
        this.duration = duration;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedDate = deletedDate;
        this.deleted = deleted;
        this.accountId = accountId;
        this.courseCategoryId = courseCategoryId;
        this.categoryName = categoryName;
        this.studentCount = studentCount;
        this.rating = rating;
        this.level = level;
        this.certificate = certificate;
        this.purchased = purchased;
    }
}
