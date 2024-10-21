package com.example.hotrohoctapbackend.DTO;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CourseDTO_User_Profile {
    private int id;
    private String duration;
    private String image;
    private String title;
    private LocalDateTime enrollment_date;

    public CourseDTO_User_Profile(int id, String duration, String image, String title, LocalDateTime  enrollment_date) {
        this.id = id;
        this.duration = duration;
        this.image = image;
        this.title = title;
        this.enrollment_date = enrollment_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime  getEnrollment_date() {
        return enrollment_date;
    }

    public void setEnrollment_date(LocalDateTime  enrollment_date) {
        this.enrollment_date = enrollment_date;
    }
}
