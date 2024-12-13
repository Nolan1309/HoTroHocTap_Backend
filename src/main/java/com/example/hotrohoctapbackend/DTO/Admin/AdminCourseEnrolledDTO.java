package com.example.hotrohoctapbackend.DTO.Admin;

public class AdminCourseEnrolledDTO {
    private int accountId;
    private String email;
    private String phone;
    private String gender;

    public AdminCourseEnrolledDTO(int accountId, String email, String phone, String gender) {
        this.accountId = accountId;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
