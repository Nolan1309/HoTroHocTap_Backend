package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Ranking.RewardDTOCreate;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    // API thêm mới phần thưởng
    @PostMapping
    public ApiResponse<RewardDTOCreate> createReward(@RequestBody RewardDTOCreate rewardDTO) {
        RewardDTOCreate createdReward = rewardService.createReward(rewardDTO);
        return new ApiResponse<>(200, "Reward created successfully", createdReward);
    }

    // API cập nhật phần thưởng
    @PutMapping("/{id}")
    public ApiResponse<RewardDTOCreate> updateReward(@PathVariable Long id, @RequestBody RewardDTOCreate rewardDTO) {
        RewardDTOCreate updatedReward = rewardService.updateReward(id, rewardDTO);
        return new ApiResponse<>(200, "Reward updated successfully", updatedReward);
    }
}