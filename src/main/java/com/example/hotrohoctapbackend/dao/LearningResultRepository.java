package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.LearningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "learningresult")
public interface LearningResultRepository extends JpaRepository<LearningResult,Integer> {
}
