package com.example.hotrohoctapbackend.DTO;

import java.util.List;

public class ChapterDTO {
    private int id;
    private String title;
    private List<LessonDTO> lessonList; // Danh sách bài học
    private List<TestDTO> testList;     // Danh sách bài kiểm tra
    private Integer id_course;          // ID của khóa học thay vì thông tin khóa học đầy đủ

    // Constructors
    public ChapterDTO() {
    }

    public ChapterDTO(int id, String title, List<LessonDTO> lessonList, List<TestDTO> testList, Integer id_course) {
        this.id = id;
        this.title = title;
        this.lessonList = lessonList;
        this.testList = testList;
        this.id_course = id_course;
    }

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LessonDTO> getLessonList() {
        return lessonList;
    }

    public void setLessonList(List<LessonDTO> lessonList) {
        this.lessonList = lessonList;
    }

    public List<TestDTO> getTestList() {
        return testList;
    }

    public void setTestList(List<TestDTO> testList) {
        this.testList = testList;
    }

    public Integer getId_course() {
        return id_course;
    }

    public void setId_course(Integer id_course) {
        this.id_course = id_course;
    }
}
