package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseInfoDetailDTO_User {
    //    private int id;
//    private String course_title;
//    private int chapter_id;
//    private String chapter_title;
//    private int lesson_id;
//    private String lesson_title;
//    private int lesson_duration;
//    private int video_id;
//    private String video_title;
//    private String video_url;
//    private String document_short;
//    private String document_url;
//    private int test_id;
//    private String test_title;
//    private String test_type;
//
//    public CourseInfoDetailDTO_User(int id, int chapter_id, String chapter_title, int lesson_id, String lesson_title,
//                                    int lesson_duration, int video_id, String video_title, String video_url,
//                                    String document_short, String document_url, int test_id,
//                                    String test_title, String test_type) {
//        this.id = id;
//        this.chapter_id = chapter_id;
//        this.chapter_title = chapter_title;
//        this.lesson_id = lesson_id;
//        this.lesson_title = lesson_title;
//        this.lesson_duration = lesson_duration;
//        this.video_id = video_id;
//        this.video_title = video_title;
//        this.video_url = video_url;
//        this.document_short = document_short;
//        this.document_url = document_url;
//        this.test_id = test_id;
//        this.test_title = test_title;
//        this.test_type = test_type;
//    }
    private int course_id;
    private String course_title;
    private List<CourseInfoDetailDTO_Chapter_User> chapters;

    public CourseInfoDetailDTO_User(int course_id, String course_title, List<CourseInfoDetailDTO_Chapter_User> chapters) {
        this.course_id = course_id;
        this.course_title = course_title;
        this.chapters = chapters;
    }

}

