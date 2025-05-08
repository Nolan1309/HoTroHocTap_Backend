package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StudentCourseProgressDTO {
    private String student_id;
    private int Age;
    private int Study_Hours_per_Week;
    private int Online_Courses_Completed;
    private double Assignment_Completion_Rate;
    private double Exam_Score;
    private double Attendance_Rate;
    private int Time_Spent_on_Social_Media;
    private int Sleep_Hours_per_Night;
    private int Gender;
    private int Preferred_Learning_Style;
    private int Participation_in_Discussions;
    private int Use_of_Educational_Tech;
    private int Self_Reported_Stress_Level;
    private Map<String, CourseProgressDTOAPI> course_progress;

    @Data
    public static class CourseProgressDTOAPI {
        private String chapter_title;
        private Double chapter_quiz;
        private Map<String, LessonProgressDTOAPI> lessons;
        private Boolean completed;

        @Data
        public static class LessonProgressDTOAPI {
            private List<LessonAttemptDTOAPI> attempts;

            @Data
            public static class LessonAttemptDTOAPI {
                private Integer videoId;
                private Integer testId;
                private Double score;
                private String timestamp;

                public LessonAttemptDTOAPI(Integer videoId, Integer testId, Double score, String timestamp) {
                    this.videoId = videoId;
                    this.testId = testId;
                    this.score = score;
                    this.timestamp = timestamp;
                }

                public LessonAttemptDTOAPI() {
                }
            }

            public LessonProgressDTOAPI() {
            }

            public LessonProgressDTOAPI(List<LessonAttemptDTOAPI> attempts) {
                this.attempts = attempts;
            }
        }
    }
}