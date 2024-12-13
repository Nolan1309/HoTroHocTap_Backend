package com.example.hotrohoctapbackend.DTO.Admin;

import java.math.BigDecimal;

public class AdminCourseResultDTO {
    private int id;
    private String courseTitle;
    private String duration;
    private BigDecimal price;
    private BigDecimal cost;
    private boolean status;
    private boolean deleted;
    private String categoryName; // Thêm trường categoryName

    // Constructors
    public AdminCourseResultDTO(int id, String courseTitle, String duration, BigDecimal price, BigDecimal cost,
                                boolean status, boolean deleted, String categoryName) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.duration = duration;
        this.price = price;
        this.cost = cost;
        this.status = status;
        this.deleted = deleted;
        this.categoryName = categoryName; // Gán giá trị categoryName
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
