package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDetailDTO_User {
    private Integer courseId;
    private String courseTitle;
    private String imageUrl;
    private BigDecimal price;

    public CourseDetailDTO_User(Integer courseId, String courseTitle, String imageUrl, BigDecimal price) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
