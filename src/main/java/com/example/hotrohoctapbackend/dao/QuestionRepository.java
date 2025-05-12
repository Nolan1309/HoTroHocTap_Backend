package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.DTO.User.UserQuestionExamDTO;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "questions")
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query(value = "SELECT q.id AS question_id, q.content, q.option_a, q.option_b, q.option_c, q.option_d, " +
            "q.created_at, q.updated_at " +
            "FROM questions q " +
            "JOIN test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId", nativeQuery = true)
    List<Object[]> findQuestionsByTestId(@Param("testId") int testId);

    @Query(value = "SELECT " +
            "q.id AS id, " +
            "q.content AS content, " +
            "q.created_at AS createdAt, " +
            "q.deleted_date AS deletedDate, " +
            "q.instruction AS instruction, " +
            "q.is_deleted AS isDeleted, " +
            "q.level AS level, " +
            "q.option_a AS optionA, " +
            "q.option_b AS optionB, " +
            "q.option_c AS optionC, " +
            "q.option_d AS optionD, " +
            "q.result AS result, " +
            "q.result_check AS resultCheck, " +
            "q.topic AS topic, " +
            "q.type AS type, " +
            "q.updated_at AS updatedAt, " +
            "q.account_id AS accountId, " +
            "q.course_id AS courseId " +
            "FROM hotrohoctap2.questions q " +
            "JOIN hotrohoctap2.test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId AND q.is_deleted = 0",
            nativeQuery = true)
    List<Object[]> findQuestionsByTestId_Exam(@Param("testId") Integer testId);

    @Query(value = "SELECT q.id AS questionId, q.instruction AS instruction, q.result AS correctShow, q.result_check AS correctCheck " +
            "FROM questions q " +
            "JOIN test_answers ta ON q.id = ta.question_id " +
            "WHERE ta.test_id = :testId", nativeQuery = true)
    List<Object[]> findQuestionsResponsiveByTestId(@Param("testId") Integer testId);

    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck, q.level , q.type, q.account_id, q.course_id FROM hotrohoctap2.questions q " +
            "WHERE q.topic IN (" +
            "    SELECT l.topic " +
            "    FROM hotrohoctap2.lessons l " +
            "    WHERE l.chapter_id = :chapterId" +
            ") " +
            "AND q.type = :type AND q.is_deleted = 0", nativeQuery = true)
    List<Object[]> findQuestionsByChapter(@Param("chapterId") Integer chapterId, @Param("type") String type);

    List<Question> findByType(String level);

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

    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck, q.level , q.type, q.account_id, q.course_id " +
            "FROM questions q where is_deleted = false ", nativeQuery = true)
    Page<Object[]> getAllQuestions(Pageable pageable);

    //    @Query(value = """
//            SELECT
//                q.id AS questionId,
//                q.content AS content,
//                q.option_a AS optionA,
//                q.option_b AS optionB,
//                q.option_c AS optionC,
//                q.option_d AS optionD,
//                q.result AS result,
//                q.instruction AS instruction,
//                q.result_check AS resultCheck,
//                q.level AS level,
//                q.type AS type,
//                q.account_id AS accountId,
//                q.course_id AS courseId,
//                q.topic as topic,
//                q.created_at as createdAt
//            FROM
//                questions q
//            WHERE
//                q.course_id = :courseId
//                AND q.account_id = :accountId
//                AND q.is_deleted = false
//                AND (:topics IS NULL OR LOWER(q.topic) IN :topics)
//                AND (:type IS NULL OR q.type = :type)
//                AND (:level IS NULL OR q.level = :level)
//                AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))
//            """,
//            countQuery = """
//                        SELECT COUNT(*)
//                        FROM questions q
//                        WHERE
//                            q.course_id = :courseId
//                            AND q.account_id = :accountId
//                            AND q.is_deleted = false
//                            AND (:topics IS NULL OR LOWER(q.topic) IN :topics)
//                            AND (:type IS NULL OR q.type = :type)
//                            AND (:level IS NULL OR q.level = :level)
//                            AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))
//                    """,
//            nativeQuery = true)
//    Page<Object[]> findQuestionsByConditions(
//            @Param("topics") List<String> topics,
//            @Param("courseId") Integer courseId,
//            @Param("accountId") Integer accountId,
//            @Param("type") String type,
//            @Param("level") String level,
//            @Param("content") String content,
//            Pageable pageable);
    @Query("SELECT q FROM Question q " +
            "WHERE q.course.id = :courseId " +
            "AND q.account.id = :accountId " +
            "AND q.isDeleted = false " +
            "AND (:topics IS NULL OR LOWER(q.topic) IN :topics) " +
            "AND (:type IS NULL OR q.type = :type) " +
            "AND (:level IS NULL OR q.level = :level) " +
            "AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))")
    Page<Question> findQuestionsByConditions(
            @Param("topics") List<String> topics,
            @Param("courseId") Integer courseId,
            @Param("accountId") Integer accountId,
            @Param("type") String type,
            @Param("level") String level,
            @Param("content") String content,
            Pageable pageable);

    @Query("SELECT q FROM Question q " +
            "WHERE q.course.id = :courseId " +
            "AND q.account.id = :accountId " +
            "AND q.isDeleted = false " +
            "AND (:type IS NULL OR q.type = :type) " +
            "AND (:level IS NULL OR q.level = :level) " +
            "AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))")
    Page<Question> findQuestionsByConditionsExam(
            @Param("courseId") Integer courseId,
            @Param("accountId") Integer accountId,
            @Param("type") String type,
            @Param("level") String level,
            @Param("content") String content,
            Pageable pageable);

    @Query("SELECT q FROM Question q " +
            "WHERE q.course.id = :courseId " +
            "AND q.account.id = :accountId " +
            "AND q.isDeleted = false " +
            "AND (:topics IS NULL OR q.topic=  :topics) " +
            "AND (:type IS NULL OR q.type = :type) " +
            "AND (:level IS NULL OR q.level = :level) " +
            "AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))")
    Page<Question> findQuestionsByConditionsBank(
            @Param("topics") String topics,
            @Param("courseId") Integer courseId,
            @Param("accountId") Integer accountId,
            @Param("type") String type,
            @Param("level") String level,
            @Param("content") String content,
            Pageable pageable);

    @Query(value = "SELECT q.id AS questionId, q.content AS content, q.option_a AS optionA, q.option_b AS optionB, q.option_c AS optionC, q.option_d AS optionD, q.result AS result, q.instruction AS instruction, q.result_check AS resultCheck " +
            "FROM questions q", nativeQuery = true)
    List<Object[]> getAllQuestionsList();

    Optional<Question> findByIdAndType(Long id, String type);


    @Query(value = "SELECT " +
            "q.type AS question_type, " +
            "COUNT(*) AS total_questions, " +
            "SUM(CASE WHEN q.level = '1' THEN 1 ELSE 0 END) AS easy_questions, " +
            "SUM(CASE WHEN q.level = '2' THEN 1 ELSE 0 END) AS medium_questions, " +
            "SUM(CASE WHEN q.level = '3' THEN 1 ELSE 0 END) AS hard_questions " +
            "FROM `hotrohoctap2`.`questions` q " +
            "WHERE q.topic IN ( " +
            "    SELECT l.topic " +
            "    FROM `hotrohoctap2`.`lessons` l " +
            "    WHERE l.chapter_id = :chapterID" +
            ") " +
            "GROUP BY q.type", nativeQuery = true)
    List<Object[]> getQuestionsCountByTypeAndLevel(@Param("chapterID") Integer chapterID);


    @Query(value = """
            SELECT q.id, q.content, q.created_at, q.deleted_date, q.instruction, q.is_deleted, q.option_a, q.option_b, q.option_c, q.option_d, q.result, q.result_check,q.updated_at, q.level, q.type, q.account_id, q.course_id\s
            FROM questions q
            WHERE q.is_deleted = 1
            AND (:courseId IS NULL OR q.course_id = :courseId)
            AND (:accountId IS NULL OR q.account_id = :accountId)
            AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))
            AND (:deletedDate IS NULL OR DATE(q.deleted_date) = :deletedDate)
            """,
            countQuery = """
                    SELECT COUNT(*) from questions q\s
                    WHERE q.is_deleted = 1
                    AND (:courseId IS NULL OR q.course_id = :courseId)
                    AND (:accountId IS NULL OR q.account_id = :accountId)
                    AND (:content IS NULL OR LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%')))
                    AND (:deletedDate IS NULL OR DATE(q.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true)
    Page<Object[]> findQuestionRestoreByCourseIdAndAccountId(
            @Param("courseId") Integer courseId,
            @Param("accountId") Integer accountId,
            @Param("content") String content,
            @Param("deletedDate") String deletedDate,
            Pageable pageable);

}
