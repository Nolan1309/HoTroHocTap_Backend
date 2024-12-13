package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "chapters")
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    //    @Query(value = "SELECT * FROM chapters WHERE course_id = :courseId", nativeQuery = true)
    @Query(value = "SELECT c.id, c.chapter_title, c.course_id, c.deleted_date, c.is_deleted FROM chapters c WHERE c.course_id = :courseId", nativeQuery = true)
    List<Chapter> findChaptersByCourseId(@Param("courseId") Integer courseId);

    List<Chapter> findByCourseId(Integer courseId);

    @Query(value = "SELECT COUNT(DISTINCT c.id) FROM chapters c WHERE c.course_id = :courseId", nativeQuery = true)
    Long countChaptersByCourseIdUser(@Param("courseId") Integer courseId);

    @Query(value = "SELECT c.id FROM chapters c WHERE c.course_id = :courseId AND c.is_deleted = false ORDER BY c.id ASC LIMIT 1", nativeQuery = true)
    Optional<Integer> findFirstChapterIdByCourseId(@Param("courseId") Integer courseId);
}
