package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "testresults")
public interface TestResultRepository extends JpaRepository<TestResult, Integer> {
    @Query(value = """
            SELECT r.test_result_id,r.completed_at,r.correct_answers, r.incorrect_answers, r.result, r.score,  r.total_questions, r.account_id, r.test_id, r.course_id,   r.deleted_date, r.is_deleted ,r.is_chapter_test, t.title 
            FROM test_results r 
            INNER JOIN tests t ON t.id = r.test_id 
            WHERE r.is_deleted = false 
              AND r.account_id = :accountId
              AND (:search IS NULL OR t.title LIKE %:search%)
            ORDER BY r.completed_at DESC
            """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM test_results r 
                    INNER JOIN tests t ON t.id = r.test_id 
                    WHERE r.is_deleted = false 
                      AND r.account_id = :accountId
                      AND (:search IS NULL OR t.title LIKE %:search%)
                    """,
            nativeQuery = true)
    Page<Object[]> findTestResultsWithTestTitle(Pageable pageable, @Param("accountId") Integer accountId, @Param("search") String search);

    @Query(value = """
            SELECT 
                (SUM(CASE 
                        WHEN tr.is_chapter_test = 0 THEN tr.score 
                        WHEN tr.is_chapter_test = 1 THEN tr.score * 2 
                     END) 
                / 
                (COUNT(CASE WHEN tr.is_chapter_test = 0 THEN 1 END) 
                + COUNT(CASE WHEN tr.is_chapter_test = 1 THEN 1 END) * 2)) AS average_score
            FROM 
                test_results tr
            WHERE 
                tr.account_id = :accountId 
                AND tr.course_id = :courseId
                AND tr.score IS NOT NULL
                AND tr.score >= 0
            """, nativeQuery = true)
    Double calculateAverageScoreUser(@Param("accountId") Long accountId, @Param("courseId") Long courseId);

    @Query(value = """
            SELECT 
              (SUM(CASE WHEN result = 'Pass' THEN 1 ELSE 0 END) / COUNT(*)) * 100 AS pass_rate
            FROM 
              test_results
            WHERE 
              account_id = :accountId AND course_id = :courseId
            """, nativeQuery = true)
    Double calculatePassRateUser(@Param("accountId") Long accountId, @Param("courseId") Long courseId);

    @Query(value = """
            SELECT 
                tr.test_result_id,
                t.title AS test_name, 
                tr.score, 
                tr.result, 
                tr.completed_at
            FROM 
                test_results tr
            JOIN 
                tests t ON tr.test_id = t.id
            WHERE 
                tr.account_id = :accountId AND t.course_id = :courseId
            ORDER BY 
                tr.completed_at DESC
            """, nativeQuery = true)
    List<Object> getTestResultsByAccountAndCourseUser(
            @Param("accountId") Long accountId,
            @Param("courseId") Long courseId
    );

    @Query(value = """
            SELECT 
                result, COUNT(*) AS total
            FROM 
                test_results
            WHERE 
                account_id = :accountId AND course_id = :courseId
            GROUP BY 
                result
            """, nativeQuery = true)
    List<Object[]> countResultsGroupedByResultUser(
            @Param("accountId") Long accountId,
            @Param("courseId") Long courseId
    );

    @Query(value = """
                SELECT 
                    q.id AS id,
                    q.content AS question,
                    q.option_a AS optionA,
                    q.option_b AS optionB,
                    q.option_c AS optionC,
                    q.option_d AS optionD,
                    q.result_check AS correctAnswer,
                    ua.result AS userAnswer
                FROM user_answers ua
                INNER JOIN questions q ON ua.question_id = q.id
                WHERE ua.account_id = :accountId AND ua.test_result_id = :testResultId
            """, nativeQuery = true)
    List<Object[]> findUserAnswersByAccountAndTestResult(
            @Param("accountId") Long accountId,
            @Param("testResultId") Long testResultId
    );

    @Query(value = "SELECT count(*) FROM test_results where test_id = :testId", nativeQuery = true)
    Long getCountTestById(@Param("testId") Integer testId);

    @Query(value = "SELECT DISTINCT tr.test_result_id, tr.is_chapter_test, tr.completed_at, tr.correct_answers, tr.deleted_date, " +
            "tr.incorrect_answers, tr.is_deleted, tr.result, tr.score, tr.total_questions, tr.account_id, tr.course_id, tr.test_id " +
            "FROM hotrohoctap2.test_results tr " +
            "JOIN hotrohoctap2.tests t ON tr.test_id = t.id " +
            "JOIN hotrohoctap2.progress p ON p.lesson_id = t.lesson_id " +
            "WHERE p.lesson_id = :lessonId " +
            "AND p.course_id = :courseId " +
            "AND tr.account_id = :accountId", nativeQuery = true)
    List<Object[]> findTestResultsByLessonIdAndAccountId(@Param("lessonId") Integer lessonId,
                                                         @Param("courseId") Integer courseId,
                                                         @Param("accountId") Integer accountId);

}
