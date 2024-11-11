package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminPaymentDetailDTO {
    private String courseName;
    private Double price;

    // Constructors
    public AdminPaymentDetailDTO(String courseName, Double price) {
        this.courseName = courseName;
        this.price = price;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
