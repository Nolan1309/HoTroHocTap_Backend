package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class AdminTestCheckInfoCourse {
    private int totalAssignedChapter;
    private int totalChapters;
    private int totalAssignedLessons;
    private int totalLessons;
    private int countAssignedTests;
    private int countUnassignedTests;
    private int countTestByCourse;
}
