package com.example.hotrohoctapbackend.DTO.AdminV2.Prediction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentExportStudentHuitDetailDTO {
    private String studentId;
    private String fullname;
    private String chapterTitle;
    private String chapterQuiz;
    private String lessonTitle;
    private Integer countTime;
    private String lessonQuiz;
    private String createDate;

    public StudentExportStudentHuitDetailDTO() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }

    public String getChapterQuiz() {
        return chapterQuiz;
    }

    public void setChapterQuiz(String chapterQuiz) {
        this.chapterQuiz = chapterQuiz;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public Integer getCountTime() {
        return countTime;
    }

    public void setCountTime(Integer countTime) {
        this.countTime = countTime;
    }

    public String getLessonQuiz() {
        return lessonQuiz;
    }

    public void setLessonQuiz(String lessonQuiz) {
        this.lessonQuiz = lessonQuiz;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
