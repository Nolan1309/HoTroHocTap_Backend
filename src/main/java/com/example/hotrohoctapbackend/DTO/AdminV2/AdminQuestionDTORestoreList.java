package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminQuestionDTORestoreList {
    private Integer id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String instruction;
    private Boolean isDeleted;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String result;
    private String resultCheck;
    private LocalDateTime updatedAt;
    private String level;
    private String type;
    private Integer accountId;
    private Integer courseId;
}
