package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource(path = "questions")
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM questions WHERE id IN (:ids)", nativeQuery = true)
    void deleteQuestionsByIds(List<Integer> ids);
    @Query(value = "SELECT q.* FROM questions q INNER JOIN test_question tq ON q.id = tq.question_id WHERE tq.test_id = :testId", nativeQuery = true)
    List<Question> findQuestionsByTestId(@Param("testId") Integer testId);
}
