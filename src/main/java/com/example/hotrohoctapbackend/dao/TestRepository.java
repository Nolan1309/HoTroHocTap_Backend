package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.entity.Test;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "tests")
public interface TestRepository extends JpaRepository<Test, Integer> {


    //    @Query(value = "SELECT * FROM tests WHERE lesson_id = :lessonId", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id, t.point, t.duration, t.easy_question, t.hard_question, t.medium_question , t.type , t.is_assigned, t.format FROM tests t WHERE t.lesson_id = :lessonId and t.is_summary = 0", nativeQuery = true)
    List<Test> findTestsByLessonId(@Param("lessonId") Integer lessonId);


    @Query("SELECT t FROM Test t " +
            "WHERE t.chapter.id = :chapterId " +
            "AND t.course.id = :courseId " +
            "AND t.isDeleted = false " +
            "AND t.isAssigned = false " +
            "AND t.isSummary = false " +
            "AND t.lesson IS NULL")
    List<Test> findTestsByChapterAndCourse(@Param("chapterId") int chapterId, @Param("courseId") int courseId);

    List<Test> findByCourseIdAndIsDeletedFalse(int courseId);

    //Truy vấn 1 bài học theo ID
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id FROM tests t WHERE t.lesson_id = :lessonId and t.is_summary = 0", nativeQuery = true)
    List<Object[]> findTestByLessonId_V2(@Param("lessonId") Integer lessonId);

    //    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id, t.easy_question, t.hard_question, t.medium_question , t.type , t.is_assigned, t.duration, t.point, t.format FROM tests t WHERE t.chapter_id = :chapterId", nativeQuery = true)
    List<Test> findTestsByChapterId(@Param("chapterId") Integer chapterId);

    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id, t.easy_question, t.hard_question, t.medium_question , t.type , t.is_assigned, t.duration, t.point, t.format FROM tests t WHERE t.chapter_id = :chapterId AND t.lesson_id is NULL AND t.is_summary = 1 AND t.is_deleted = 0", nativeQuery = true)
    List<Test> findTestsByChapterIdAndIsChapterTest(@Param("chapterId") Integer chapterId);

//    SELECT `id`, `created_at`, `deleted_date`, `description`, `is_deleted`, `is_summary`, `title`, `total_question`, `updated_at`, `chapter_id`, `course_id`, `lesson_id`, `easy_question`, `hard_question`, `medium_question`, `type`, `is_assigned` FROM `tests` WHERE 1

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM tests WHERE id = :testId", nativeQuery = true)
    int deleteTest(@Param("testId") Integer testId);


    //    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId AND lesson_id IS NULL", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id, t.easy_question, t.hard_question, t.medium_question , t.type , t.is_assigned, t.duration, t.point, t.format FROM tests t WHERE t.chapter_id = :chapterId AND t.lesson_id IS NULL and t.is_summary = 1", nativeQuery = true)
    Test findChapterTestByChapterId(@Param("chapterId") Integer chapterId);

    //    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
//            "FROM tests t", nativeQuery = true)
//    List<Object[]> findAllTestSummaries();
    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt , t.is_deleted  , t.is_summary, t.lesson_id, t.course_id, t.chapter_id \n" +
            "            FROM tests t", nativeQuery = true)
    Page<Object[]> findAllTestSummaries(Pageable pageable);

    @Query(value = """
                SELECT 
                    t.id,
                    t.title,
                    t.total_question AS totalQuestion,
                    t.easy_question as easyQuestion,
                    t.medium_question as mediumQuestion,
                    t.hard_question as hardQuestion,
                    t.type,
                    t.created_at AS createdAt,
                    t.is_deleted,
                    t.is_summary,
                    t.lesson_id,
                    t.chapter_id,
                    t.course_id,
                    t.description,
                    t.is_assigned,
                    t.duration,
                    t.format,
                    t.point
                FROM 
                    tests t
                WHERE 
                    t.course_id = :courseId AND t.is_deleted = 0
                    AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%'))
                    AND t.format = 'test'
            """,
            countQuery = """
                        SELECT 
                            COUNT(*) 
                        FROM 
                            tests t
                        WHERE 
                            t.course_id = :courseId AND t.is_deleted = 0
                            AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%'))
                            AND t.format = 'test'
                    """,
            nativeQuery = true)
    Page<Object[]> findByCourseIdAndTitleWithPagination(
            @Param("courseId") Integer courseId,
            @Param("title") String title,
            Pageable pageable
    );


    @Query(value = """
                SELECT 
                    t.id,
                    t.title,
                    t.total_question AS totalQuestion,
                    t.easy_question as easyQuestion,
                    t.medium_question as mediumQuestion,
                    t.hard_question as hardQuestion,
                    t.type,
                    t.created_at AS createdAt,
                    t.is_deleted,
                    t.is_summary,
                    t.lesson_id,
                    t.chapter_id,
                    t.course_id,
                    t.description,
                    t.is_assigned,
                    t.duration,
                    t.format,
                    t.point
                FROM 
                    tests t
                WHERE 
                    t.course_id = :courseId AND t.is_deleted = 0
                    AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%'))
                    AND t.format = 'exam'
            """,
            countQuery = """
                        SELECT 
                            COUNT(*) 
                        FROM 
                            tests t
                        WHERE 
                            t.course_id = :courseId AND t.is_deleted = 0
                            AND (:title IS NULL OR t.title LIKE CONCAT('%', :title, '%'))
                            AND t.format = 'exam'
                    """,
            nativeQuery = true)
    Page<Object[]> findByCourseIdAndTitleWithPaginationExam(
            @Param("courseId") Integer courseId,
            @Param("title") String title,
            Pageable pageable
    );

    @Query("SELECT t FROM Test t WHERE t.format = 'exam' AND t.isDeleted = false AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND (:courseId IS NULL OR t.course.id = :courseId)")
    Page<Test> findFiltered(@Param("title") String title, @Param("courseId") Integer courseId, Pageable pageable);

    @Query("SELECT t FROM Test t WHERE t.format = 'exam' AND t.isDeleted = false AND (:courseId IS NULL OR t.course.id = :courseId)")
    List<Test> findFilteredExaList(@Param("courseId") Integer courseId);


    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
            "FROM tests t", nativeQuery = true)
    List<Object[]> findAllTestSummariesList();

    @Query(value = "SELECT id, created_at, deleted_date, description, is_deleted, is_summary, title, total_question, updated_at, chapter_id, course_id, lesson_id, duration, easy_question, hard_question, medium_question, is_assigned, type FROM tests WHERE course_id = :courseId AND chapter_id = :chapterId AND is_summary = 0", nativeQuery = true)
    List<Object[]> findTestsByCourseAndChapter(@Param("courseId") Integer courseId, @Param("chapterId") Integer chapterId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Test t " +
            "WHERE t.course.id = :courseId " +
            "AND t.chapter.id = :chapterId " +
            "AND t.isSummary = true " +
            "AND t.lesson.id IS NULL")
    boolean existsByCourseIdAndChapterIdAndIsSummary(@Param("courseId") Integer courseId,
                                                     @Param("chapterId") Integer chapterId);

    // Tổng số chương đã phân bổ bài kiểm tra (test chương)
    @Query(value = "SELECT COUNT(DISTINCT chapter_id) FROM tests WHERE is_summary = true AND lesson_id IS NULL AND course_id = :courseId", nativeQuery = true)
    int countAssignedChapters(@Param("courseId") int courseId);

    // Tổng số chương (không bị xóa) của khóa học
    @Query(value = "SELECT COUNT(*) FROM chapters WHERE course_id = :courseId AND is_deleted = false", nativeQuery = true)
    int countTotalChapters(@Param("courseId") int courseId);

    // Tổng số bài học đã phân bổ bài kiểm tra (test bài học)
    @Query(value = "SELECT COUNT(chapter_id) FROM tests WHERE is_summary = false AND lesson_id IS NOT NULL AND course_id = :courseId", nativeQuery = true)
    int countAssignedLessons(@Param("courseId") int courseId);

    // Tổng số bài học (không bị xóa) của khóa học
    @Query(value = "SELECT COUNT(*) FROM lessons WHERE course_id = :courseId AND is_deleted = false", nativeQuery = true)
    int countTotalLessons(@Param("courseId") int courseId);

    @Query(value = "SELECT COUNT(*) FROM tests WHERE course_id = :courseId AND is_deleted = 0 AND is_assigned = 1", nativeQuery = true)
    int countAssignedTests(@Param("courseId") int courseId);

    // Đếm số bài kiểm tra chưa phân bổ
    @Query(value = "SELECT COUNT(*) FROM tests WHERE course_id = :courseId AND is_deleted = 0 AND is_assigned = 0", nativeQuery = true)
    int countUnassignedTests(@Param("courseId") int courseId);

    @Query(value = "SELECT COUNT(*) FROM tests WHERE course_id = :courseId AND is_deleted = 0", nativeQuery = true)
    int countTestsByCourse(@Param("courseId") int courseId);

    @Query(value = """
            SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id, t.easy_question, t.hard_question, t.medium_question, t.type, t.is_assigned\s
            FROM tests t\s
            WHERE t.is_deleted = 1
            AND (:courseId IS NULL OR t.course_id = :courseId)
            AND (:chapterId IS NULL OR t.chapter_id = :chapterId)
            AND (:testTitle IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :testTitle, '%')))
            AND (:deletedDate IS NULL OR DATE(t.deleted_date) = :deletedDate)
            """,
            countQuery = """
                    SELECT COUNT(*) from tests t\s
                    WHERE t.is_deleted = 1
                    AND (:courseId IS NULL OR t.course_id = :courseId)
                    AND (:chapterId IS NULL OR t.chapter_id = :chapterId)
                    AND (:testTitle IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :testTitle, '%')))
                    AND (:deletedDate IS NULL OR DATE(t.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true)
    Page<Object[]> findTestsRestore(
            @Param("courseId") Integer courseId,
            @Param("chapterId") Integer chapterId,
            @Param("testTitle") String testTitle,
            @Param("deletedDate") String deletedDate,
            Pageable pageable);

    @Query("SELECT t FROM Test t WHERE t.isDeleted = true " +
            "AND (:courseId IS NULL OR t.course.id = :courseId) " +
            "AND (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Test> findExamDeleted(
            @Param("courseId") Integer courseId,
            @Param("title") String title,
            Pageable pageable);


    @Query(value = "SELECT " +
            "t.id AS testId, " +
            "t.title, " +
            "t.description, " +
            "t.duration, " +
            "t.type AS testType, " +
            "COUNT(ta.id) AS totalQuestions " +
            "FROM hotrohoctap2.tests t " +
            "LEFT JOIN hotrohoctap2.test_answers ta ON t.id = ta.test_id " +
            "WHERE t.course_id = :courseId " +
            "AND t.format = 'exam' " +
            "AND t.is_deleted = 0 " +
            "GROUP BY t.id " +
            "ORDER BY t.created_at DESC",
            countQuery = "SELECT COUNT(*) FROM hotrohoctap2.tests t WHERE t.course_id = :courseId AND t.format = 'exam' AND t.is_deleted = 0",
            nativeQuery = true)
    Page<Object[]> findTestsWithQuestionCountByCourseId(@Param("courseId") Integer courseId, Pageable pageable);

    List<Test> findByCourseIdAndFormatAndIsDeletedFalse(Integer courseId, String format);

    @Query("SELECT t FROM Test t " +
            "LEFT JOIN TestEnrollment te ON te.test.id = t.id " +
            "LEFT JOIN ExamInfo ei ON ei.test.id = t.id " +
            "WHERE t.format = 'exam' " +
            "AND t.isDeleted = false " +
            "AND ei.status = 'ACTIVE' " +
            "AND (:courseId IS NULL OR t.course.id = :courseId) " +
            "AND (:title IS NULL OR t.title LIKE %:title%) " +
            "GROUP BY t.id " +
            "ORDER BY COUNT(te.id) DESC")
    Page<Test> findByCourseAndTitleContaining(@Param("courseId") Integer courseId,
                                              @Param("title") String title,
                                              Pageable pageable);


    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.test.id = :testId AND r.reviewType = 'TEST'")
    Double findAverageRatingByTestId(Integer testId);
}
