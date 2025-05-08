package com.example.hotrohoctapbackend.DTO.AdminV2.Prediction;

import lombok.Data;

@Data
public class StudentCourseDataHuitDTO {
    private String studentId;
    private String fullName;
    private String email;
    private Integer age;
    private String gender;

    public StudentCourseDataHuitDTO() {
    }

    public StudentCourseDataHuitDTO(String studentId, String fullName, String email, Integer age, String gender) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.age = age;
        this.gender = gender;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
