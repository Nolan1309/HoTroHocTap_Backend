package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class CourseInfoDetailDTO_Video_User {
    private int video_id;
    private String video_title;
    private String video_url;
    private String document_short;
    private String document_url;

    public CourseInfoDetailDTO_Video_User(int video_id, String video_title, String video_url, String document_short, String document_url) {
        this.video_id = video_id;
        this.video_title = video_title;
        this.video_url = video_url;
        this.document_short = document_short;
        this.document_url = document_url;
    }
}
