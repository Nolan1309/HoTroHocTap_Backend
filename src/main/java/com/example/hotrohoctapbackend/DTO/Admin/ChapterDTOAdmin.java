package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

@Data
public class ChapterDTOAdmin {
    private int id;
    private String title;
    private Integer course_id;
    private Boolean deleted;
}
