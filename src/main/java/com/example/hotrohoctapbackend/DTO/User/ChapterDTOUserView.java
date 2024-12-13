package com.example.hotrohoctapbackend.DTO.User;

import java.util.List;

public class ChapterDTOUserView {
    private Integer chapterId;
    private String chapterTitle;
    private Long lessonCount;
    public List<VideoDTOUserView> videoDTOUserViewList;

    public ChapterDTOUserView(Integer chapterId, String chapterTitle, Long lessonCount) {
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.lessonCount = lessonCount;
    }

    public List<VideoDTOUserView> getVideoDTOUserViewList() {
        return videoDTOUserViewList;
    }

    public void setVideoDTOUserViewList(List<VideoDTOUserView> videoDTOUserViewList) {
        this.videoDTOUserViewList = videoDTOUserViewList;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public Long getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(Long lessonCount) {
        this.lessonCount = lessonCount;
    }
}
