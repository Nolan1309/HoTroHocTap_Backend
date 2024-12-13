package com.example.hotrohoctapbackend.dao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.hotrohoctapbackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "lessons")
public interface LessonRepository extends JpaRepository<Lesson,Integer> {
    @Query(value = "SELECT * FROM lessons WHERE chapter_id = :chapterId", nativeQuery = true)
    List<Lesson> findLessonsByChapterId(@Param("chapterId") Integer chapterId);

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
            "l.is_deleted AS deleted "+
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
}
