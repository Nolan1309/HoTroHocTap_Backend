package com.example.hotrohoctapbackend.DTO.User;

import lombok.Getter;

public class VideoDTOUserView {

    private Integer videoId;
    private String videoTitle;
    private Integer videoDuration;
    private Boolean isPreview;

    @Getter
    private String linkVideo;

    // Constructor, getters, setters
    public VideoDTOUserView(Integer videoId, String videoTitle, Integer videoDuration, Boolean viewTest, String linkVideo) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.isPreview = viewTest;
        this.linkVideo = linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public Boolean getPreview() {
        return isPreview;
    }

    public void setPreview(Boolean preview) {
        isPreview = preview;
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
