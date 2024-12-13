package com.example.hotrohoctapbackend.DTO.Admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class AdminDiscounAddDTO {
    private String title;
    private String description;
    private BigDecimal discount_value;
    private LocalDateTime start_date;
    private LocalDateTime end_date;

    // Getters and Setters
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

    public BigDecimal getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(BigDecimal discount_value) {
        this.discount_value = discount_value;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }
}
