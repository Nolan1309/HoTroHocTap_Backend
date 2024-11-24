package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminCourseOfDiscount {

    private Integer id;
    private String coursesTitle;
    private String duration;
    private Double price;
    private Double cost;

    // Constructor
    public AdminCourseOfDiscount(Integer id, String coursesTitle, String duration, Double price, Double cost) {
        this.id = id;
        this.coursesTitle = coursesTitle;
        this.duration = duration;
        this.price = price;
        this.cost = cost;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCoursesTitle() {
        return coursesTitle;
    }

    public void setCoursesTitle(String coursesTitle) {
        this.coursesTitle = coursesTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    // toString() for Debugging
    @Override
    public String toString() {
        return "AdminCourseOfDiscount{" +
                "id=" + id +
                ", coursesTitle='" + coursesTitle + '\'' +
                ", duration='" + duration + '\'' +
                ", price=" + price +
                ", cost=" + cost +
                '}';
    }
}
