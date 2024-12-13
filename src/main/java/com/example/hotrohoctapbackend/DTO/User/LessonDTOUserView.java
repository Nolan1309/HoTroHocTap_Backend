package com.example.hotrohoctapbackend.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class LessonDTOUserView {
    private Long lessonId;
    private String lessonTitle;
    private Integer lessonDuration;
    private Integer totalVideoDuration; // Tổng thời lượng video trong bài học
    private Integer videoCount;         // Tổng số video trong bài học
    private List<VideoDTOUserView> videos;      // Danh sách video

    // Constructor, getters, setters
    public LessonDTOUserView(Long lessonId, String lessonTitle, Integer lessonDuration) {
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.lessonDuration = lessonDuration;
        this.videos = new ArrayList<>();
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public Integer getLessonDuration() {
        return lessonDuration;
    }

    public void setLessonDuration(Integer lessonDuration) {
        this.lessonDuration = lessonDuration;
    }

    public Integer getTotalVideoDuration() {
        return totalVideoDuration;
    }

    public void setTotalVideoDuration(Integer totalVideoDuration) {
        this.totalVideoDuration = totalVideoDuration;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public List<VideoDTOUserView> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoDTOUserView> videos) {
        this.videos = videos;
    }
}
