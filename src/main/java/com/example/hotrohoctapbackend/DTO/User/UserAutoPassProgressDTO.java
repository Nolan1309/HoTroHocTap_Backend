package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class UserAutoPassProgressDTO {
    private Integer accountId;
    private Integer courseId;
    private Integer chapterId;
    private Integer lessonId;
    private boolean videoStatus;
    private boolean testStatus;
    private boolean isChapterTest;
}
