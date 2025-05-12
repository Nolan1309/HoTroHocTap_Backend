package com.example.hotrohoctapbackend.DTO.AdminV3.Account;

import lombok.Data;

@Data
public class AccountDTOAdminCreate {
    private String fullname;
    private String email;
    private String phone;
    private String password;
    private String role;
    private Integer roleId;
    private String status;
    private String image;

    public AccountDTOAdminCreate() {
    }

    public AccountDTOAdminCreate(String fullname, String email, String phone, String password, String role, Integer roleId, String status, String image) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.roleId = roleId;
        this.status = status;
        this.image = image;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
