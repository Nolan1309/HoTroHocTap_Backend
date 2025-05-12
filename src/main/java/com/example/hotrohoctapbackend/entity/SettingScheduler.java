package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.ReminderType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "setting")
public class SettingScheduler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "settingId")
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "reminder_type")
    private ReminderType reminderType;


    @Column(name = "reminder_time")
    private String reminderTime;

    @Column(name = "name")
    private String name;
    
    @Column(name = "isCheck")
    private boolean isCheck;

    @Column(name = "created_at")
    private String createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = java.time.LocalDateTime.now().toString();
    }
}
