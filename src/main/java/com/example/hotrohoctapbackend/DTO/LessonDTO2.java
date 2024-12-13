package com.example.hotrohoctapbackend.DTO;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonDTO2 {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer duration;  // Đơn vị tính là phút
    private int chapter_id;
    private Integer course_id;

    // Constructors
    public LessonDTO2() {
    }

    public LessonDTO2(int id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, Integer duration, int chapter_id, Integer course_id) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.duration = duration;
        this.chapter_id = chapter_id;
        this.course_id = course_id;
    }


}
