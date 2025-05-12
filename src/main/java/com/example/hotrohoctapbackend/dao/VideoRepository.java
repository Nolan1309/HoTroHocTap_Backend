package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Video;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "videos")
public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query(value = "SELECT * FROM videos WHERE lesson_id = :lessonId", nativeQuery = true)
    Video findVideoByLessonId(@Param("lessonId") Integer lessonId);

    @Query("SELECT v FROM Video v " +
            "JOIN v.lesson l " +
            "JOIN l.course c " +
            "WHERE c.id = :courseId AND v.isDeleted = false")
    List<Video> findByCourseIdAndIsDeletedFalse(@Param("courseId") int courseId);

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

    @Query(value = "SELECT v.id, v.created_at, v.deleted_date, v.document_short, v.document_url, v.duration, v.is_deleted, v.video_title, v.updated_at, v.url, v.lesson_id, v.isviewtest " +
            "FROM videos v " +
            "INNER JOIN lessons l ON v.lesson_id = l.id " +
            "WHERE l.course_id = :courseId AND l.is_deleted = 0", nativeQuery = true)
    List<Object[]> findVideosByCourseId(@Param("courseId") Integer courseId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE videos v SET v.isviewtest = :isViewTest WHERE v.id = :videoId", nativeQuery = true)
    int updateVideoStatus(@Param("videoId") Integer videoId, @Param("isViewTest") Boolean isViewTest);

    List<Video> findByLessonCourseIdAndIsDeletedFalse(Integer courseId);
}
