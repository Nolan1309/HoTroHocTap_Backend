package com.example.hotrohoctapbackend.DTO.User;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CourseDTO_User_Profile {
    private int id;
    private Integer duration;
    private String image;
    private String title;
    private LocalDateTime enrollment_date;
    private Boolean status;
    private Boolean isDeleted;

    public CourseDTO_User_Profile(int id, Integer duration, String image, String title, LocalDateTime enrollment_date, Boolean status, Boolean isDeleted) {
        this.id = id;
        this.duration = duration;
        this.image = image;
        this.title = title;
        this.enrollment_date = enrollment_date;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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

    public LocalDateTime getEnrollment_date() {
        return enrollment_date;
    }

    public void setEnrollment_date(LocalDateTime enrollment_date) {
        this.enrollment_date = enrollment_date;
    }
}
