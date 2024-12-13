package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

@Data
public class DashboardReportDto {

    private double totalRevenueToday;
    private int totalDocuments;
    private int totalCourses;
    private int totalQuestions;
    private int totalUsers;
    private int totalTeachers;

    public DashboardReportDto(double totalRevenueToday, int totalDocuments, int totalCourses,
                              int totalQuestions, int totalUsers, int totalTeachers) {
        this.totalRevenueToday = totalRevenueToday;
        this.totalDocuments = totalDocuments;
        this.totalCourses = totalCourses;
        this.totalQuestions = totalQuestions;
        this.totalUsers = totalUsers;
        this.totalTeachers = totalTeachers;
    }

    // Getters và Setters
}
