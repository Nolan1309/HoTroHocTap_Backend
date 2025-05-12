package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class CourseCodeCreateDTO {
    private int quantity;
    private int courseId;
    private Integer accountId;  // Có thể là null
    private int expiryDays;

    public CourseCodeCreateDTO(int quantity, int courseId, Integer accountId, int expiryDays) {
        this.quantity = quantity;
        this.courseId = courseId;
        this.accountId = accountId;
        this.expiryDays = expiryDays;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public int getExpiryDays() {
        return expiryDays;
    }

    public void setExpiryDays(int expiryDays) {
        this.expiryDays = expiryDays;
    }

    public CourseCodeCreateDTO() {
    }
}
