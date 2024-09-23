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


}
