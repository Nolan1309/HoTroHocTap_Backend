package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class AccountSendNotification_User {
    private Integer userId;
    private String userName;
    private String email;

    public AccountSendNotification_User(Integer userId, String userName,String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
    }
}
