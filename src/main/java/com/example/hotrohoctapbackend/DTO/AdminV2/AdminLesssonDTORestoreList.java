package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminLesssonDTORestoreList {
    private Integer id;
    private LocalDateTime createdAt;
    private Integer duration;
    private String lessonTitle;
    private LocalDateTime updatedAt;
    private Integer chapterId;
    private Integer courseId;
    private LocalDateTime deletedDate;
    private Boolean isDeleted;
    private String isTestExcluded;
}
