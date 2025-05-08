package com.example.hotrohoctapbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "student_course_data")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StudentCourseData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "student_code")
    private String studentId;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "class_room")
    private String classRoom;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "age")
    private int age;

    @Column(name = "study_hours_per_week")
    private int studyHoursPerWeek;

    @Column(name = "online_courses_completed")
    private int onlineCoursesCompleted;

    @Column(name = "assignment_completion_rate")
    private double assignmentCompletionRate;

    @Column(name = "exam_score")
    private double examScore;

    @Column(name = "attendance_rate")
    private double attendanceRate;

    @Column(name = "time_spent_on_social_media")
    private int timeSpentOnSocialMedia;

    @Column(name = "sleep_hours_per_night")
    private int sleepHoursPerNight;

    @Column(name = "gender")  // 0: Female, 1: Male
    private int gender;

    @Column(name = "preferred_learning_style")
    private int preferredLearningStyle;

    @Column(name = "participation_in_discussions")
    private int participationInDiscussions;

    @Column(name = "use_of_educational_tech")
    private int useOfEducationalTech;

    @Column(name = "self_reported_stress_level")
    private int selfReportedStressLevel;

    @Column(name = "course_progress", columnDefinition = "TEXT")
    private String courseProgress;  // Store course progress as a JSON string
}
