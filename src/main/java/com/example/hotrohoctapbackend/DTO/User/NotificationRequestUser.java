package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class NotificationRequestUser {
    private String title;
    private String message;
    private String topic;
    private Long userId;
}
