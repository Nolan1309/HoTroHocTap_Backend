package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.RewardType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reward_history")
@Data
public class RewardHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  // ID của phần thưởng

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;  // Người dùng nhận thưởng

    @ManyToOne
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;  // Xếp hạng liên kết với phần thưởng

    @ManyToOne
    @JoinColumn(name = "ranking_id", nullable = false)
    private Ranking ranking;

    @Column(name = "reward_name")
    private String rewardName;

    @Column(name = "reward_type")
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // Thời gian trao thưởng

    // Getters and Setters
}
