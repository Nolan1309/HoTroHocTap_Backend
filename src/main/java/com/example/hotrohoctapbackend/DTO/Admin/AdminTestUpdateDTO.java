    package com.example.hotrohoctapbackend.DTO.Admin;
    import com.google.api.client.util.DateTime;
    import lombok.Data;

    import java.time.LocalDateTime;
    import java.util.Date;

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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
