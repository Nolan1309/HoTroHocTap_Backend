package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "ranking")
    private int ranking;

    @Column(name = "totalPoints")
    private int totalPoints;

    @Column(name = "updateTimestamp")
    private LocalDateTime updateTimestamp; // Thời gian cập nhật xếp hạng

    @Column(name = "type")
    private String type;
}
