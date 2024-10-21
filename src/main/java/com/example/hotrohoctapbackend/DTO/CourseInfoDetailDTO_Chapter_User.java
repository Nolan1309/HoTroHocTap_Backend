package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseInfoDetailDTO_Chapter_User {
    private int chapter_id;
    private String chapter_title;
    private List<CourseInfoDetailDTO_Lesson_User> lessons;
    private CourseInfoDetailDTO_Test_User chapter_test; // Bài kiểm tra chương

    public CourseInfoDetailDTO_Chapter_User(int chapter_id, String chapter_title, List<CourseInfoDetailDTO_Lesson_User> lessons, CourseInfoDetailDTO_Test_User chapter_test) {
        this.chapter_id = chapter_id;
        this.chapter_title = chapter_title;
        this.lessons = lessons;
        this.chapter_test = chapter_test;
    }
}
