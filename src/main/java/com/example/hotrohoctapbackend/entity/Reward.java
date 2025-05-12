package com.example.hotrohoctapbackend.entity;

import com.example.hotrohoctapbackend.enums.ReviewStatus;
import com.example.hotrohoctapbackend.enums.ReviewType;
import com.example.hotrohoctapbackend.enums.RewardCategoryType;
import com.example.hotrohoctapbackend.enums.RewardType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reward")
@Data
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;  // ID của phần thưởng

    @Column(name = "reward_name")
    private String rewardName;  // Tên phần thưởng

    @Column(name = "reward_value")
    private BigDecimal rewardValue;  // Giá trị phần thưởng (tiền, điểm,...)

    @Column(name = "reward_type")
    @Enumerated(EnumType.STRING)
    private RewardType rewardType;  // Loại phần thưởng (WEEKLY, MONTHLY)

    @Column(name = "reward_category_type")
    @Enumerated(EnumType.STRING)
    private RewardCategoryType rewardCategoryType;  // Loại phần thưởng (WEEKLY, MONTHLY)

    @Column(name = "rank_position")
    private int rankPosition;  // Vị trí xếp hạng nhận thưởng (1, 2, 3,...)

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
