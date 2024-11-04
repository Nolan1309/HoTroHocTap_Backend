    package com.example.hotrohoctapbackend.DTO;
    import lombok.Data;

    @Data
    public class AdminTestUpdateDTO {
        private Integer id;
        private String title;
        private String description;
        private Integer lessonId;
        private Integer chapterId;
        private Integer courseId;
        private Integer totalQuestion;
        private Boolean isSummary;
    }
