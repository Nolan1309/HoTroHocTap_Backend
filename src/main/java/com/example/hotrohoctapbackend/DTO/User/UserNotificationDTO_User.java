package com.example.hotrohoctapbackend.DTO.User;

import com.example.hotrohoctapbackend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class UserNotificationDTO_User {
    private Notification notification;
    private boolean readStatus;
}
