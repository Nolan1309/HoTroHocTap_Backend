package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AdminAccount_V2 {
    private int id;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String email;
    private String fullname;
    private String gender;
    private String googleId; // ID từ Google
    private boolean isDeleted;
    private boolean isGoogleAccount;
    private String phone;
    private LocalDateTime updatedAt;
    private Integer roleId; // Tên vai trò của người dùng

    public AdminAccount_V2(int id, LocalDateTime birthday, LocalDateTime createdAt, LocalDateTime deletedDate, String email, String fullname, String gender, String googleId, boolean isDeleted, boolean isGoogleAccount, String phone, LocalDateTime updatedAt, Integer roleId) {
        this.id = id;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.deletedDate = deletedDate;
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.googleId = googleId;
        this.isDeleted = isDeleted;
        this.isGoogleAccount = isGoogleAccount;
        this.phone = phone;
        this.updatedAt = updatedAt;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public boolean isGoogleAccount() {
        return isGoogleAccount;
    }

    public void setGoogleAccount(boolean googleAccount) {
        isGoogleAccount = googleAccount;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
