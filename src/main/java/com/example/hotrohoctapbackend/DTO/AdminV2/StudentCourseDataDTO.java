package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class StudentCourseDataDTO {
    private Integer id;
    private String studentId;
    private Integer accountId;
    private String email;
    private String fullname;
    private String classRoom;
    private Integer courseId;

    private Integer age;
    private Integer studyHoursPerWeek;
    private Integer onlineCoursesCompleted;
    private Double assignmentCompletionRate;
    private Double examScore;
    private Double attendanceRate;
    private Integer timeSpentOnSocialMedia;
    private Integer sleepHoursPerNight;
    private Integer gender;  // 0: Female, 1: Male
    private Integer preferredLearningStyle;
    private Integer participationInDiscussions;
    private Integer useOfEducationalTech;
    private Integer selfReportedStressLevel;
    private String courseProgress;
}
