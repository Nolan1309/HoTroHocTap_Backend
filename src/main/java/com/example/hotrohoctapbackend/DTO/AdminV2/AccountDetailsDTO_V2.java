package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class AccountDetailsDTO_V2 {
    private Integer id; // ID của tài khoản
    private String fullname; // Họ và tên của tài khoản
    private String email; // Email của tài khoản
    private String phone; // Số điện thoại
    private String gender; // Giới tính
    private String googleId; // Google ID nếu tài khoản liên kết Google
    private String image; // Đường dẫn hình ảnh của tài khoản
    private Boolean isDeleted; // Trạng thái tài khoản bị xóa
    private Boolean isGoogleAccount; // Có phải tài khoản Google không
    private String birthday; // Ngày sinh (định dạng chuỗi)
    private String createdAt; // Ngày tạo tài khoản
    private String updatedAt; // Ngày cập nhật tài khoản
    private String deletedDate; // Ngày tài khoản bị xóa
    private Integer roleId; // ID vai trò của tài khoản

    public AccountDetailsDTO_V2() {
    }

    public AccountDetailsDTO_V2(Integer id, String fullname, String email, String phone, String gender, String googleId, String image, Boolean isDeleted, Boolean isGoogleAccount, String birthday, String createdAt, String updatedAt, String deletedDate, Integer roleId) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.googleId = googleId;
        this.image = image;
        this.isDeleted = isDeleted;
        this.isGoogleAccount = isGoogleAccount;
        this.birthday = birthday;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedDate = deletedDate;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getGoogleAccount() {
        return isGoogleAccount;
    }

    public void setGoogleAccount(Boolean googleAccount) {
        isGoogleAccount = googleAccount;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
