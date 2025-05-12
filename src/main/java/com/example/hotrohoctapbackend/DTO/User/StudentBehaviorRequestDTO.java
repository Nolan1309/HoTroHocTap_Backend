package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class StudentBehaviorRequestDTO {
    private String email;
    private String code;
    private Integer accountId;
    private Integer birthday;
    private int studyHoursPerWeek;
    private int timeSpentOnSocialMedia;
    private int sleepHoursPerNight;
    private String gender;
    private int preferredLearningStyle;
    private boolean useOfEducationalTech;
    private int selfReportedStressLevel;
}
