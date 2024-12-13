package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "videos")
public interface VideoRepository extends JpaRepository<Video,Integer> {
    @Query(value = "SELECT * FROM videos WHERE lesson_id = :lessonId", nativeQuery = true)
    Video findVideoByLessonId(@Param("lessonId") Integer lessonId);


    @Query(value = """
        SELECT v.* FROM courses c
        JOIN chapters ch ON ch.course_id = c.id
        JOIN lessons l ON l.chapter_id = ch.id
        JOIN videos v ON v.lesson_id = l.id
        WHERE c.id = :courseId
        ORDER BY ch.id, l.id, v.id
        LIMIT 1
    """, nativeQuery = true)
    Video findFirstVideoByCourseId(@Param("courseId") int courseId);
}
