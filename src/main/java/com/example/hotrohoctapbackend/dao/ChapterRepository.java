package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "chapters")
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    //    @Query(value = "SELECT * FROM chapters WHERE course_id = :courseId", nativeQuery = true)
    @Query(value = "SELECT c.id, c.chapter_title, c.course_id, c.deleted_date, c.is_deleted, c.status FROM chapters c WHERE c.course_id = :courseId and c.is_deleted = 0", nativeQuery = true)
    List<Chapter> findChaptersByCourseId(@Param("courseId") Integer courseId);

    //    @Query(value = "SELECT c.id, c.chapter_title, c.course_id, c.deleted_date, c.is_deleted,  FROM chapters c WHERE c.course_id = :courseId and c.is_deleted = 0", nativeQuery = true)
//    List<Chapter> findByCourseId(Integer courseId);
    List<Chapter> findByCourseIdAndIsDeletedFalse(int courseId);


    @Query(value = "SELECT COUNT(DISTINCT c.id) FROM chapters c WHERE c.course_id = :courseId", nativeQuery = true)
    Long countChaptersByCourseIdUser(@Param("courseId") Integer courseId);

    @Query(value = "SELECT c.id FROM chapters c WHERE c.course_id = :courseId AND c.is_deleted = false ORDER BY c.id ASC LIMIT 1", nativeQuery = true)
    Optional<Integer> findFirstChapterIdByCourseId(@Param("courseId") Integer courseId);

    List<Chapter> findByIsDeletedTrue();

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1\s
            ORDER BY ch.deleted_date DESC
             """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1\s
                    """,
            nativeQuery = true)
    Page<Object[]> findDeletedChapterAll(Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted
            FROM chapters ch
            WHERE ch.is_deleted = 1 
            AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%') \n
            ORDER BY ch.deleted_date DESC
            """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 
                    AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByTitle(String chapterTittle, Pageable pageable);


    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1 
            AND DATE(ch.deleted_date) = :deletedDate
            ORDER BY ch.deleted_date DESC;
            """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 
                    AND DATE(ch.deleted_date) = :deletedDate
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByDeletedDate(String deletedDate, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1 
            AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
            AND (DATE(ch.deleted_date) = :deletedDate OR :deletedDate IS NULL)
            ORDER BY ch.deleted_date DESC;
            """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 
                    AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
                    AND (DATE(ch.deleted_date) = :deletedDate OR :deletedDate IS NULL)
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByChapterTitleAndDeleteDate(String chapterTittle, String deletedDate, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
             WHERE ch.is_deleted = 1 and ch.course_id = :courseId 
            ORDER BY ch.deleted_date DESC
             """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 and ch.course_id = :courseId 
                    ORDER BY ch.deleted_date DESC;
                    """,
            nativeQuery = true)
    Page<Object[]> findDeletedChapterByCourseId(@Param("courseId") Integer courseId, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1 and ch.course_id = :courseId
            AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
            ORDER BY ch.deleted_date DESC
            """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 and ch.course_id = :courseId
                    AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByTitleByCourseId(@Param("courseId") Integer courseId, String chapterTittle, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1 and ch.course_id = :courseId
            AND DATE(ch.deleted_date) = :deletedDate
            ORDER BY ch.deleted_date DESC;
            """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 and ch.course_id = :courseId
                    AND DATE(ch.deleted_date) = :deletedDate
                    ORDER BY ch.deleted_date DESC;
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByDeletedDateByCourseId(@Param("courseId") Integer courseId, String deletedDate, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted\s
            FROM chapters ch
            WHERE ch.is_deleted = 1 and ch.course_id = :courseId
            AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
            AND (DATE(ch.deleted_date) = :deletedDate OR :deletedDate IS NULL)
            ORDER BY ch.deleted_date DESC;
            """,
            countQuery = """
                    SELECT COUNT(*)\s
                    FROM chapters ch
                    WHERE ch.is_deleted = 1 and ch.course_id = :courseId
                    AND ch.chapter_title LIKE CONCAT('%', :chapterTittle, '%')
                    AND (DATE(ch.deleted_date) = :deletedDate OR :deletedDate IS NULL)
                    """,
            nativeQuery = true)
    Page<Object[]> searchChapterByChapterTitleAndDeleteDateByCourseId(@Param("courseId") Integer courseId, String chapterTittle, String deletedDate, Pageable pageable);

    @Query(value = """
            SELECT ch.id, ch.chapter_title, ch.course_id, ch.deleted_date, ch.is_deleted
            FROM chapters ch
            WHERE ch.is_deleted = 0
            AND (:courseId IS NULL OR ch.course_id = :courseId);
            """,
            countQuery = """
                     SELECT COUNT(*)
                            FROM chapters ch
                            WHERE ch.is_deleted = 0 AND (:courseId IS NULL OR ch.course_id = :courseId);
                    """,
            nativeQuery = true)
    List<Object[]> findNoDeletedChaptersList(@Param("courseId") Integer courseId);
}
