package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.AdminV3.Ranking.RewardDTOCreate;
import com.example.hotrohoctapbackend.dao.RewardRepository;

import com.example.hotrohoctapbackend.entity.Reward;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RewardService {

    @Autowired
    private RewardRepository rewardRepository;

    public RewardDTOCreate createReward(RewardDTOCreate rewardDTO) {
        Reward reward = new Reward();
        reward.setRewardName(rewardDTO.getRewardName());
        reward.setRewardValue(rewardDTO.getRewardValue());
        reward.setRewardType(rewardDTO.getRewardType());
        reward.setRewardCategoryType(rewardDTO.getRewardCategoryType());
        reward.setRankPosition(rewardDTO.getRankPosition());
        reward.setCreatedAt(LocalDateTime.now());
        reward.setUpdatedAt(LocalDateTime.now());

        reward = rewardRepository.save(reward);
        return convertToDTO(reward);
    }

    public RewardDTOCreate updateReward(Long id, RewardDTOCreate rewardDTO) {
        Reward reward = rewardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Reward not found"));

        reward.setRewardName(rewardDTO.getRewardName());
        reward.setRewardValue(rewardDTO.getRewardValue());
        reward.setRewardType(rewardDTO.getRewardType());
        reward.setRewardCategoryType(rewardDTO.getRewardCategoryType());
        reward.setRankPosition(rewardDTO.getRankPosition());
        reward.setUpdatedAt(LocalDateTime.now());

        reward = rewardRepository.save(reward);
        return convertToDTO(reward);
    }

    private RewardDTOCreate convertToDTO(Reward reward) {
        RewardDTOCreate rewardDTO = new RewardDTOCreate();
        rewardDTO.setId(reward.getId());
        rewardDTO.setRewardName(reward.getRewardName());
        rewardDTO.setRewardValue(reward.getRewardValue());
        rewardDTO.setRewardType(reward.getRewardType());
        rewardDTO.setRewardCategoryType(reward.getRewardCategoryType());
        rewardDTO.setRankPosition(reward.getRankPosition());
        rewardDTO.setCreatedAt(reward.getCreatedAt());
        rewardDTO.setUpdatedAt(reward.getUpdatedAt());
        return rewardDTO;
    }
}
