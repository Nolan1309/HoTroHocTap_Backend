package com.example.hotrohoctapbackend.DTO.AdminV3.Student;

import lombok.Data;

@Data
public class StudentDTO {
    private String id;
    private String fullname;
    private String email;
    private String phone;
    private String enrollmentDate;
    private Double progress;
    private String status;
    private String type;

    public StudentDTO() {
    }

    public StudentDTO(String id, String fullname, String email, String phone, String enrollmentDate, Double progress, String status, String type) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.enrollmentDate = enrollmentDate;
        this.progress = progress;
        this.status = status;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
