package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class CourseCodeActivationRequestDTO {
    private Integer accountId;
    private String code;
    private String email;
    private String studentId;
    private int age;
    private int studyHoursPerWeek;
    private int timeSpentOnSocialMedia;
    private int sleepHoursPerNight;
    private int gender;
    private int preferredLearningStyle;
    private int useOfEducationalTech;
    private int selfReportedStressLevel;
    private String courseProgress;
}
