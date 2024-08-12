package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Test_Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "test_questions")
public interface Test_QuestionRepository extends JpaRepository<Test_Question,Integer> {
}
