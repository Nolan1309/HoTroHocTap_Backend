package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "tests")
public interface TestRepository extends JpaRepository<Test, Integer> {


    //    @Query(value = "SELECT * FROM tests WHERE lesson_id = :lessonId", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id FROM tests t WHERE t.lesson_id = :lessonId", nativeQuery = true)
    List<Test> findTestsByLessonId(@Param("lessonId") Integer lessonId);

    //    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id FROM tests t WHERE t.chapter_id = :chapterId", nativeQuery = true)
    List<Test> findTestsByChapterId(@Param("chapterId") Integer chapterId);

    //    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId AND lesson_id IS NULL", nativeQuery = true)
    @Query(value = "SELECT t.id, t.created_at, t.deleted_date, t.description, t.is_deleted, t.is_summary, t.title, t.total_question, t.updated_at, t.chapter_id, t.course_id, t.lesson_id FROM tests t WHERE t.chapter_id = :chapterId AND t.lesson_id IS NULL", nativeQuery = true)
    Test findChapterTestByChapterId(@Param("chapterId") Integer chapterId);

    //    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
//            "FROM tests t", nativeQuery = true)
//    List<Object[]> findAllTestSummaries();
    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt , t.is_deleted  , t.is_summary, t.lesson_id, t.course_id \n" +
            "            FROM tests t", nativeQuery = true)
    Page<Object[]> findAllTestSummaries(Pageable pageable);

    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
            "FROM tests t", nativeQuery = true)
    List<Object[]> findAllTestSummariesList();

    @Query(value = "SELECT id, created_at, deleted_date, description, is_deleted, is_summary, title, total_question, updated_at, chapter_id, course_id, lesson_id FROM tests WHERE course_id = :courseId AND chapter_id = :chapterId AND is_summary = 0", nativeQuery = true)
    List<Test> findTestsByCourseAndChapter(@Param("courseId") Integer courseId, @Param("chapterId") Integer chapterId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Test t " +
            "WHERE t.course.id = :courseId " +
            "AND t.chapter.id = :chapterId " +
            "AND t.isSummary = true " +
            "AND t.lesson.id IS NULL")
    boolean existsByCourseIdAndChapterIdAndIsSummary(@Param("courseId") Integer courseId,
                                                     @Param("chapterId") Integer chapterId);


}
