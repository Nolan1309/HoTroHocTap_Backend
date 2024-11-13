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

    @Column(name = "deletedDate")
    private LocalDateTime deletedDate;

    @Column(name = "isDeleted")
    private boolean isDeleted = false; // Đặt mặc định là false

    @PrePersist
    protected void onCreate() {
        if (deletedDate == null) {
            deletedDate = LocalDateTime.now(); // Đặt giá trị mặc định là ngày hiện tại khi tạo mới
        }
    }
}