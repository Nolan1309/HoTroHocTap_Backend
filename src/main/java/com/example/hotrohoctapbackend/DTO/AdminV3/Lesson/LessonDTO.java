package com.example.hotrohoctapbackend.DTO.AdminV3.Lesson;


import lombok.Data;

@Data
public class LessonDTO {
    private int id;
    private String createdAt;
    private Integer duration;
    private String lessonTitle;
    private String updatedAt;
    private int chapterId;
    private int courseId;
    private String deletedDate;
    private Boolean isDeleted;
    private String isTestExcluded;
    private String topic;
    private Boolean status;
}