package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class ExistProgressPassDTO_USER {
    private Integer courseId;
    private Integer accountId;
    private Integer chapterId;
    private Integer lessonId;
    private boolean chapterTest;
}
