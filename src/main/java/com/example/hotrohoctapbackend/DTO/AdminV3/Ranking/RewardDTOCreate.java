package com.example.hotrohoctapbackend.DTO.AdminV3.Ranking;

import com.example.hotrohoctapbackend.enums.RewardCategoryType;
import com.example.hotrohoctapbackend.enums.RewardType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RewardDTOCreate {
    private Long id;
    private String rewardName;
    private BigDecimal rewardValue;
    private RewardType rewardType;
    private RewardCategoryType rewardCategoryType;
    private int rankPosition;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
