package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
}