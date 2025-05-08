package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class AdminVideoDTOEditCourseList {
    @Getter
    private Integer id ;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime deletedDate;
    @Getter
    private String documentShort;
    @Getter
    private String documentUrl ;
    @Getter
    private Integer duration;
    private  Boolean isDeleted;
    @Getter
    private String videoTitle;
    @Getter
    private LocalDateTime updatedAt;
    @Getter
    private String videoUrl;
    @Getter
    private Integer lessonId;
    private Boolean isViewTest;

    public AdminVideoDTOEditCourseList() {
    }

    public AdminVideoDTOEditCourseList(Integer id, LocalDateTime createdAt, LocalDateTime deletedDate, String documentShort, String documentUrl, Integer duration, Boolean isDeleted, String videoTitle, LocalDateTime updatedAt, String videoUrl, Integer lessonId, Boolean isViewTest) {
        this.id = id;
        this.createdAt = createdAt;
        this.deletedDate = deletedDate;
        this.documentShort = documentShort;
        this.documentUrl = documentUrl;
        this.duration = duration;
        this.isDeleted = isDeleted;
        this.videoTitle = videoTitle;
        this.updatedAt = updatedAt;
        this.videoUrl = videoUrl;
        this.lessonId = lessonId;
        this.isViewTest = isViewTest;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public void setDocumentShort(String documentShort) {
        this.documentShort = documentShort;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public Boolean getViewTest() {
        return isViewTest;
    }

    public void setViewTest(Boolean viewTest) {
        isViewTest = viewTest;
    }
}
