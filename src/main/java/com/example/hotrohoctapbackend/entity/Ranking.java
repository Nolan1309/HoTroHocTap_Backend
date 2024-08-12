package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "rankings")
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "overall_score")
    private int overall_score;

    @Column(name = "total_score")
    private int total_score;

    @Column(name = "ranking")
    private int ranking;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;
}
