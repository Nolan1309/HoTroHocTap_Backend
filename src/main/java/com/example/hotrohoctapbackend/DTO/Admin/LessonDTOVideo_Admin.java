package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class LessonDTOVideo_Admin {
    private int id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer duration;  // Đơn vị tính là phút
    private int chapter_id;
    private Integer course_id;

    private Integer video_id;
    private String video_title;
    private String video_url;

    private String document_short;
    private String document_url;

    private Integer test_id;
    private String test_title;

}
