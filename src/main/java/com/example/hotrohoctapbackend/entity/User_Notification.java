package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_notifications")
public class User_Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(name = "read_status")
    private boolean read_status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

}