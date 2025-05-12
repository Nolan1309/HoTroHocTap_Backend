package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseDetailDTO {


    private int id;
    private String author;
    private BigDecimal cost;
    private String courseOutput;
    private LocalDateTime createdAt;
    private String description;
    private Integer duration;
    private String image_url;
    private String language;
    private BigDecimal price;
    private Boolean status;
    private String title;
    private LocalDateTime updatedAt;
    private int course_category_id;
    private Integer accountId;

    public CourseDetailDTO() {
    }

    public CourseDetailDTO(int id, String author, BigDecimal cost, String courseOutput, LocalDateTime createdAt, String description, Integer duration, String image_url, String language, BigDecimal price, Boolean status, String title, LocalDateTime updatedAt, int course_category_id) {
        this.id = id;
        this.author = author;
        this.cost = cost;
        this.courseOutput = courseOutput;
        this.createdAt = createdAt;
        this.description = description;
        this.duration = duration;
        this.image_url = image_url;
        this.language = language;
        this.price = price;
        this.status = status;
        this.title = title;
        this.updatedAt = updatedAt;
        this.course_category_id = course_category_id;
    }
}
