package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Entity
@Table(name = "user_activity_logs")
@Data

public class ActivityLogs {
    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "status")
    private String activity_description;

    @Column(name = "activity_date")
    private LocalDateTime activity_date;

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
