package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AccountTeacherDTO_V2 {
    private int id;
    private LocalDateTime birthday;
    private LocalDateTime createdAt;
    private LocalDateTime deletedDate;
    private String email;
    private String fullname;
    private String gender;
    private boolean isDeleted;
    private String phone;
    private LocalDateTime updatedAt;
    private int roleId;

    public AccountTeacherDTO_V2(int id, LocalDateTime birthday, LocalDateTime createdAt, LocalDateTime deletedDate, String email, String fullname, String gender, boolean isDeleted, String phone, LocalDateTime updatedAt, int roleId) {
        this.id = id;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.deletedDate = deletedDate;
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.isDeleted = isDeleted;
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

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
