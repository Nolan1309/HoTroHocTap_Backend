package com.example.hotrohoctapbackend.DTO.AdminV3.Notification;

import com.example.hotrohoctapbackend.util.TOPIC;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTOResponsive {
    private int id;
    private String title;
    private String message;
    private TOPIC topic;
    private LocalDateTime createdAt;
    private Boolean status;

    public NotificationDTOResponsive(int id, String title, String message, TOPIC topic, LocalDateTime createdAt, Boolean status) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.topic = topic;
        this.createdAt = createdAt;
        this.status = status;
    }
}
