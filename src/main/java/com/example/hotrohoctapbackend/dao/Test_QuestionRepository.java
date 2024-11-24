package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Test_Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "test_questions")
public interface Test_QuestionRepository extends JpaRepository<Test_Question,Integer> {
    @Query(value = "SELECT * FROM test_answers WHERE test_id = :testId AND question_id = :questionId LIMIT 1", nativeQuery = true)
    Optional<Test_Question> findByTestIdAndQuestionId(@Param("testId") Integer testId, @Param("questionId") Integer questionId);

}
