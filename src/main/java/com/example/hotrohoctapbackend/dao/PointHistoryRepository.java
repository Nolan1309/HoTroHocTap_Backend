package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "point_history")
public interface PointHistoryRepository extends JpaRepository<PointHistory, Integer> {
}
