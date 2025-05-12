package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.DeliveryStatus;
import com.example.hotrohoctapbackend.util.TOPIC;
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

    @Column(name = "title")
    private String title;

    @Column(name = "read_status")
    private boolean read_status;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "schedule_time")
    private LocalDateTime scheduleTime; // Lịch cá nhân hóa (ví dụ: 30 phút trước buổi học)

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic", nullable = false)
    private TOPIC topic;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}