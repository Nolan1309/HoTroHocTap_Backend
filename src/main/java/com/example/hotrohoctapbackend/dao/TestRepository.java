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
public interface TestRepository extends JpaRepository<Test,Integer> {


    @Query(value = "SELECT * FROM tests WHERE lesson_id = :lessonId", nativeQuery = true)
    List<Test> findTestsByLessonId(@Param("lessonId") Integer lessonId);
    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId", nativeQuery = true)
    List<Test> findTestsByChapterId(@Param("chapterId") Integer chapterId);

    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId AND lesson_id IS NULL", nativeQuery = true)
    Test findChapterTestByChapterId(@Param("chapterId") Integer chapterId);
//    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
//            "FROM tests t", nativeQuery = true)
//    List<Object[]> findAllTestSummaries();
@Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
        "FROM tests t", nativeQuery = true)
Page<Object[]> findAllTestSummaries(Pageable pageable);
    @Query(value = "SELECT t.id, t.title, t.total_question AS totalQuestion, t.created_at AS createdAt, t.is_deleted " +
            "FROM tests t", nativeQuery = true)
    List<Object[]> findAllTestSummariesList();
}

