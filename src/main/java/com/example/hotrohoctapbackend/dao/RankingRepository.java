package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "rankings")
public interface RankingRepository extends JpaRepository<Ranking,Integer> {
}
