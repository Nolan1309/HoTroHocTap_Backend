package com.example.hotrohoctapbackend.DTO.Admin;

import java.time.LocalDateTime;

public class AdminDicountDetailDTO {
    private int id;
    private String description;
    private double discountValue;
    private String title;
    private LocalDateTime endDate;
    private LocalDateTime startDate;

    // Constructor
    public AdminDicountDetailDTO(int id, String description, double discountValue, String title, LocalDateTime endDate, LocalDateTime startDate) {
        this.id = id;
        this.description = description;
        this.discountValue = discountValue;
        this.title = title;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
