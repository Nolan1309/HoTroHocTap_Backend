package com.example.hotrohoctapbackend.DTO.AdminV3.Chapter;

import lombok.Data;

@Data
public class ChapterDTOAdminV3 {
    private String id;
    private Integer course_id;
    private String chapter_title;
    private Boolean status;
    private String deletedDate;
    private Boolean deleted;

    public ChapterDTOAdminV3() {
    }

    public ChapterDTOAdminV3(String id, Integer course_id, String chapter_title, Boolean status, String deletedDate, Boolean deleted) {
        this.id = id;
        this.course_id = course_id;
        this.chapter_title = chapter_title;
        this.status = status;
        this.deletedDate = deletedDate;
        this.deleted = deleted;
    }
}
