package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminChapterDTORestoreList {
    private Integer id;
    private String chapterTitle;
    private Integer courseId;
    private LocalDateTime deletedDate;
    private Boolean isDeleted;
}
