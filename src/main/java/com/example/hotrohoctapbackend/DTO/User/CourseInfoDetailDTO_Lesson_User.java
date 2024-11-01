package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class CourseInfoDetailDTO_Lesson_User {
    private int lesson_id;
    private String lesson_title;
    private int lesson_duration;
    private CourseInfoDetailDTO_Video_User video;
    private CourseInfoDetailDTO_Test_User lesson_test;

    public CourseInfoDetailDTO_Lesson_User(int lesson_id, String lesson_title, int lesson_duration, CourseInfoDetailDTO_Video_User video, CourseInfoDetailDTO_Test_User lesson_test) {
        this.lesson_id = lesson_id;
        this.lesson_title = lesson_title;
        this.lesson_duration = lesson_duration;
        this.video = video;
        this.lesson_test = lesson_test;
    }
}
