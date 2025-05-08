package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminDocumentDTORestoreList {
    private Integer id;
    private LocalDateTime createdAt;
    private String description;
    private String image;
    private String title;
    private LocalDateTime updatedAt;
    private String url;
    private Integer view;
    private Integer idCategory;
    private LocalDateTime deletedDate;
    private Boolean isDeleted;
    private Boolean status;
}
