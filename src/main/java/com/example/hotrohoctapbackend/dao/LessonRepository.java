package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.hotrohoctapbackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "lessons")
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    //    @Query(value = "SELECT * FROM lessons WHERE chapter_id = :chapterId", nativeQuery = true)
    @Query(value = "SELECT l.id, l.created_at, l.lesson_title, l.updated_at, l.chapter_id, l.course_id, l.deleted_date, l.is_deleted,  l.is_test_excluded, l.topic, l.status, l.duration FROM lessons l WHERE l.chapter_id = :chapterId and l.is_deleted = 0", nativeQuery = true)
    List<Lesson> findLessonsByChapterId(@Param("chapterId") Integer chapterId);

    @Query(value = "SELECT l.id, l.created_at, l.duration, l.lesson_title, l.updated_at, l.chapter_id, l.course_id, l.deleted_date, l.is_deleted , l.is_test_excluded, l.topic\n" +
            "            FROM lessons l \n" +
            "            WHERE l.course_id = :courseId AND l.is_deleted = 0 and l.is_test_excluded = 'EMPTYTEST'\n" +
            "             AND (:chapterId IS NULL OR l.chapter_id = :chapterId)", nativeQuery = true)
    List<Object[]> findLessonsByChapterId_V2EMPTYTEST(@Param("chapterId") Integer chapterId, @Param("courseId") Integer courseId);

    @Query(value = "SELECT l.id, l.created_at, l.duration, l.lesson_title, l.updated_at, l.chapter_id, l.course_id, l.deleted_date, l.is_deleted, l.is_test_excluded, l.topic, l.status " +
            "FROM lessons l " +
            "WHERE l.course_id = :courseId AND l.is_deleted = 0 " +
            "AND (:chapterId IS NULL OR l.chapter_id = :chapterId)",
            countQuery = "SELECT COUNT(*) FROM lessons l " +
                    "WHERE l.course_id = :courseId AND l.is_deleted = 0 " +
                    "AND (:chapterId IS NULL OR l.chapter_id = :chapterId)",
            nativeQuery = true)
    Page<Object[]> findLessonsByChapterId_V2Page(
            @Param("chapterId") Integer chapterId,
            @Param("courseId") Integer courseId,
            Pageable pageable
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE lessons v SET v.is_test_excluded = :isExcluded WHERE v.id = :lessonId", nativeQuery = true)
    int updateExcludedLesson(@Param("lessonId") Integer lessonId, @Param("isExcluded") String isExcluded);

    List<Lesson> findByChapter_IdAndCourse_Id(int chapterId, int courseId);

    @Query(value = "SELECT " +
            "l.id AS id, " +
            "l.lesson_title AS title, " +
            "l.created_at AS createdAt, " +
            "l.updated_at AS updatedAt, " +
            "l.duration AS duration, " +
            "l.chapter_id AS chapter_id, " +
            "l.course_id AS course_id, " +
            "v.id AS video_id, " +
            "v.video_title AS video_title, " +
            "v.url AS video_url, " +
            "v.document_short AS document_short, " +
            "v.document_url AS document_url, " +
            "t.id AS test_id, " +
            "l.topic AS topic, " +
            "t.title AS test_title " +
            "FROM lessons l " +
            "LEFT JOIN videos v ON l.id = v.lesson_id " +
            "LEFT JOIN tests t ON l.id = t.lesson_id " +
            "WHERE l.id = :lessonId",  // Thêm điều kiện WHERE
            nativeQuery = true)
    List<Object[]> findLessonVideoTestDataByLessonId(@Param("lessonId") int lessonId);

    @Query(value = "SELECT COUNT(DISTINCT l.id) FROM lessons l WHERE l.course_id = :courseId", nativeQuery = true)
    Long countsLessonsByCourseIdUser(@Param("courseId") Integer courseId);

    @Query(value = "SELECT " +
            "l.id AS lesson_id, " +
            "l.lesson_title AS lesson_title, " +
            "c.courses_title AS course_name, " +
            "ch.chapter_title AS chapter_name," +
            "l.is_deleted AS deleted " +
            "FROM lessons l " +
            "LEFT JOIN courses c ON l.course_id = c.id " +
            "LEFT JOIN chapters ch ON l.chapter_id = ch.id",
            countQuery = "SELECT COUNT(*) " +
                    "FROM lessons l " +
                    "LEFT JOIN courses c ON l.course_id = c.id " +
                    "LEFT JOIN chapters ch ON l.chapter_id = ch.id",
            nativeQuery = true)
    Page<Object[]> findLessonCourseChapterData(Pageable pageable);

    @Query(value = "SELECT " +
            "l.id AS lesson_id, " +
            "l.lesson_title AS lesson_title, " +
            "c.courses_title AS course_name, " +
            "ch.chapter_title AS chapter_name, " +
            "l.is_deleted AS deleted " +
            "FROM lessons l " +
            "LEFT JOIN courses c ON l.course_id = c.id " +
            "LEFT JOIN chapters ch ON l.chapter_id = ch.id",
            nativeQuery = true)
    List<Object[]> findLessonCourseChapterDataList();


    @Query(value = "SELECT l.id FROM lessons l WHERE l.chapter_id = :chapterId AND l.is_deleted = false ORDER BY l.id ASC LIMIT 1", nativeQuery = true)
    Optional<Integer> findFirstLessonIdByChapterId(@Param("chapterId") Integer chapterId);

    List<Lesson> findByIsDeletedTrue();

    @Query(value = """
             SELECT l.id, l.created_at, l.duration, l.lesson_title, l.updated_at, 
                    l.chapter_id, l.course_id, l.deleted_date, l.is_deleted, l.is_test_excluded
             FROM lessons l
             WHERE l.is_deleted = 1
             AND (:courseId IS NULL OR l.course_id = :courseId)
             AND (:chapterId IS NULL OR l.chapter_id = :chapterId)
            AND (:lessonTitle IS NULL OR LOWER(l.lesson_title) LIKE LOWER(CONCAT('%', :lessonTitle, '%')))
             AND (:deletedDate IS NULL OR DATE(l.deleted_date) = :deletedDate)
             """,
            countQuery = """
                    SELECT COUNT(*) FROM lessons l
                    WHERE l.is_deleted = 1
                    AND (:courseId IS NULL OR l.course_id = :courseId)
                    AND (:chapterId IS NULL OR l.chapter_id = :chapterId)
                    AND (:lessonTitle IS NULL OR LOWER(l.lesson_title) LIKE LOWER(CONCAT('%', :lessonTitle, '%')))
                    AND (:deletedDate IS NULL OR DATE(l.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true)
    Page<Object[]> findLessons(
            @Param("courseId") Integer courseId,
            @Param("chapterId") Integer chapterId,
            @Param("lessonTitle") String lessonTitle,
            @Param("deletedDate") String deletedDate,
            Pageable pageable);


    @Query(value = "SELECT l.id, l.created_at, l.duration, l.lesson_title, l.updated_at, " +
            "l.chapter_id, l.course_id, l.deleted_date, l.is_deleted, l.is_test_excluded, l.topic, l.status " +
            "FROM lessons l JOIN tests t ON l.id = t.lesson_id " +
            "WHERE t.id = :testId AND t.lesson_id = :lessonId AND t.is_summary = 0 AND t.chapter_id = :chapterId AND t.is_assigned = 1", nativeQuery = true)
    List<Object[]> findLessonByTestIdAndLessonId(
            @Param("testId") Integer testId,
            @Param("lessonId") Integer lessonId,
            @Param("chapterId") Integer chapterId);

    @Query("SELECT l FROM Lesson l WHERE l.chapter.id = :chapterId AND l.isDeleted = false")
    List<Lesson> findLessonsByChapterIdAndIsDeleted(@Param("chapterId") Integer chapterId);

    @Query(value = "SELECT l.id, l.created_at, l.duration, l.lesson_title, l.updated_at, " +
            "l.chapter_id, l.course_id, l.deleted_date, l.is_deleted, l.is_test_excluded, l.topic, l.status " +
            "FROM lessons l " +
            "WHERE l.id = :lessonId", nativeQuery = true)
    List<Object[]> findLessonByLessonId(@Param("lessonId") Integer lessonId);

    @Query("SELECT l FROM Lesson l WHERE l.course.id = :courseId AND l.isDeleted = false")
    List<Lesson> findLessonsByCourseId(int courseId);
}
