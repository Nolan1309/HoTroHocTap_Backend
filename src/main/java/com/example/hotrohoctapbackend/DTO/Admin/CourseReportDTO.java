package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseReportDTO {
    private String courseName;
    private Long students;
    private BigDecimal revenue;
    private Boolean status;
    private String authorName;

    public CourseReportDTO(String courseName, Long students, BigDecimal revenue, Boolean status, String authorName) {
        this.courseName = courseName;
        this.students = students;
        this.revenue = revenue;
        this.status = status;
        this.authorName = authorName;
    }
}
