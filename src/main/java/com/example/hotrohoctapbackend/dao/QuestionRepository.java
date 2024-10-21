package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "questions")
public interface QuestionRepository extends JpaRepository<Question,Integer> {
    @Query(value = "SELECT q.id AS question_id, q.content, q.option_a, q.option_b, q.option_c, q.option_d, " +
            "q.created_at, q.updated_at " +
            "FROM questions q " +
            "JOIN test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId", nativeQuery = true)
    List<Object[]> findQuestionsByTestId(@Param("testId") int testId);
}
