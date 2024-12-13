package com.example.hotrohoctapbackend.DTO.User;

public class VideoDTOUserView {

    private Integer videoId;
    private String videoTitle;
    private Integer videoDuration;
    private Boolean viewTest;

    // Constructor, getters, setters
    public VideoDTOUserView(Integer videoId, String videoTitle, Integer videoDuration,Boolean viewTest) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.viewTest = viewTest;
    }

    public Boolean getViewTest() {
        return viewTest;
    }

    public void setViewTest(Boolean viewTest) {
        this.viewTest = viewTest;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public Integer getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(Integer videoDuration) {
        this.videoDuration = videoDuration;
    }
}
