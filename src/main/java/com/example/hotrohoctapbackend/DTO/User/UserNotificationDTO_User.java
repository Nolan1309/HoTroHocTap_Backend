package com.example.hotrohoctapbackend.DTO.User;

import com.example.hotrohoctapbackend.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class UserNotificationDTO_User {
    private Notification notification;
    private boolean readStatus;

    public UserNotificationDTO_User() {
    }

    public UserNotificationDTO_User(Notification notification, boolean readStatus) {
        this.notification = notification;
        this.readStatus = readStatus;
    }
}
