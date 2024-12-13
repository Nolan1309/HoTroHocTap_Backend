package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
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
    @Query(
            value = "SELECT q.* FROM questions q INNER JOIN test_answers tq ON q.id = tq.question_id WHERE tq.test_id = :testId",
            countQuery = "SELECT COUNT(*) FROM questions q INNER JOIN test_answers tq ON q.id = tq.question_id WHERE tq.test_id = :testId",
            nativeQuery = true
    )
    Page<Question> findQuestionsByTestIdAdmin(@Param("testId") Integer testId, Pageable pageable);
    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck " +
            "FROM questions q " +
            "WHERE q.id = :id",
            nativeQuery = true)
    List<Object[]> getQuestionDetailsById(@Param("id") int id);
    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck " +
            "FROM questions q", nativeQuery = true)
    Page<Object[]> getAllQuestions(Pageable pageable);

    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck " +
            "FROM questions q", nativeQuery = true)
    List<Object[]> getAllQuestionsList();
}
