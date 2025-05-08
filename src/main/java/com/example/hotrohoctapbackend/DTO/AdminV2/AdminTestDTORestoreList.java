package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminTestDTORestoreList {
    private Integer id;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String description;
    private Boolean isDeleted;
    private Boolean isSummary;
    private String title;
    private Integer totalQuestion;
    private LocalDateTime updatedAt;
    private Integer chapterId;
    private Integer courseId;
    private Integer lessonId;
    private Integer easyQuestion;
    private Integer hardQuestion;
    private Integer mediumQuestion;
    private String type;
    private Boolean isAssigned;
    private Integer duration;
}
