package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class VideoDTO_User {
    private int id;
    private String title;
    private String url;
    private String documentShort;
    private String documentUrl;
    private Integer duration;
    private Integer lesson_id;
}
