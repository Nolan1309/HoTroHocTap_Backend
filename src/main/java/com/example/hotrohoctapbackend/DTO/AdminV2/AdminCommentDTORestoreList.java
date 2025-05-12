package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminCommentDTORestoreList {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private Boolean isApproved;
    private Boolean isDeleted;
    private LocalDateTime updatedAt;
    private Integer accId;
    private Integer contentId;
    private Integer lessonId;
    private Integer videoId;

}
