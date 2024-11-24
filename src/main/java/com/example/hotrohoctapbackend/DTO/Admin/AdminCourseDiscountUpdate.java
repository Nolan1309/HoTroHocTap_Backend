package com.example.hotrohoctapbackend.DTO.Admin;

import java.math.BigDecimal;

public class AdminCourseDiscountUpdate {
    private Integer courseId;
    private Integer discountId;
    private BigDecimal discountValue; // Giá trị giảm giá, có thể lấy từ Discount entity

    // Getters và Setters
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
}
