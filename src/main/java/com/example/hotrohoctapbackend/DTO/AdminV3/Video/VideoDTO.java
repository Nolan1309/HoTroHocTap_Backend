package com.example.hotrohoctapbackend.DTO.AdminV3.Video;

import lombok.Data;

@Data
public class VideoDTO {
    private String id;
    private String lesson_id;
    private String video_title;

    private String url;
    private String documentShort;
    private String documentUrl;
    private Integer duration;
    private String createdAt;
    private String updatedAt;
    private String deletedDate;
    private Boolean deleted;
    private Boolean isViewTest;

    public VideoDTO() {
    }

    public VideoDTO(String id, String lesson_id, String video_title, String url, String documentShort, String documentUrl, Integer duration, String createdAt, String updatedAt, String deletedDate, Boolean deleted, Boolean isViewTest) {
        this.id = id;
        this.lesson_id = lesson_id;
        this.video_title = video_title;

        this.url = url;
        this.documentShort = documentShort;
        this.documentUrl = documentUrl;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedDate = deletedDate;
        this.deleted = deleted;
        this.isViewTest = isViewTest;
    }
}
