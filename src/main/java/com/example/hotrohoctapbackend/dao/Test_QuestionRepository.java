package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Test_Question;
import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "test_questions")
public interface Test_QuestionRepository extends JpaRepository<Test_Question, Integer> {

    @Query(value = "SELECT q.* FROM questions q JOIN test_question tq ON q.id = tq.question_id WHERE tq.test_id = :testId", nativeQuery = true)
    List<Question> findQuestionsByTestId(@Param("testId") int testId);
}
