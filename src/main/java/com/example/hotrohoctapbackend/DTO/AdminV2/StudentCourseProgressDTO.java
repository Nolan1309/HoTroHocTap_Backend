package com.example.hotrohoctapbackend.DTO.AdminV2;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class StudentCourseProgressDTO {
    private Map<String, CourseProgressDTOAPI> course_progress;

    public Map<String, CourseProgressDTOAPI> getCourse_progress() {
        return course_progress;
    }

    public void setCourse_progress(Map<String, CourseProgressDTOAPI> course_progress) {
        this.course_progress = course_progress;
    }

    public static class CourseProgressDTOAPI {
        private String chapter_title;
        private Double chapter_quiz;
        private Map<String, Object> lessons;
        private Boolean completed;

        // Getters and Setters
        public String getChapter_title() {
            return chapter_title;
        }

        public void setChapter_title(String chapter_title) {
            this.chapter_title = chapter_title;
        }

        public Double getChapter_quiz() {
            return chapter_quiz;
        }

        public void setChapter_quiz(Double chapter_quiz) {
            this.chapter_quiz = chapter_quiz;
        }

        public Map<String, Object> getLessons() {
            return lessons;
        }

        public void setLessons(Map<String, Object> lessons) {
            this.lessons = lessons;
        }

        public Boolean getCompleted() {
            return completed;
        }

        public void setCompleted(Boolean completed) {
            this.completed = completed;
        }
    }
}
