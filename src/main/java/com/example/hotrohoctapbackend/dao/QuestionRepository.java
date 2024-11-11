package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryRestResource(path = "questions")
public interface QuestionRepository extends JpaRepository<Question,Integer> {
    @Query(value = "SELECT q.id AS question_id, q.content, q.option_a, q.option_b, q.option_c, q.option_d, " +
            "q.created_at, q.updated_at " +
            "FROM questions q " +
            "JOIN test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId", nativeQuery = true)
    List<Object[]> findQuestionsByTestId(@Param("testId") int testId);


    @Query(value = "SELECT q.id AS questionId, q.instruction AS instruction, q.result AS correctShow, q.result_check AS correctCheck " +
            "FROM questions q " +
            "JOIN test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId", nativeQuery = true)
    List<Object[]> findQuestionsResponsiveByTestId(@Param("testId") Integer testId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM questions WHERE id IN (:ids)", nativeQuery = true)
    void deleteQuestionsByIds(List<Integer> ids);
    @Query(value = "SELECT q.* FROM questions q INNER JOIN test_question tq ON q.id = tq.question_id WHERE tq.test_id = :testId", nativeQuery = true)
    List<Question> findQuestionsByTestIdAdmin(@io.lettuce.core.dynamic.annotation.Param("testId") Integer testId);
}
