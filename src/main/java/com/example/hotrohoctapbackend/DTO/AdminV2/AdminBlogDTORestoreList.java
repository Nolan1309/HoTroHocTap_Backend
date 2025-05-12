package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminBlogDTORestoreList    {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String image;
    private Boolean isDeleted;
    private Boolean status;
    private String title;
    private LocalDateTime updatedAt;
    private Integer authorId;
    private Integer catBlogId;
}
