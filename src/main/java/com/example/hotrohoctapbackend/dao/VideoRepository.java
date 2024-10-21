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
}
