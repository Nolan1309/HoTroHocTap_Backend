package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "questions")
public interface QuestionRepository extends JpaRepository<Question,Integer> {
}
