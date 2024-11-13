package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

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



}
