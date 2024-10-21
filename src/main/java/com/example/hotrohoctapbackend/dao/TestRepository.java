package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "tests")
public interface TestRepository extends JpaRepository<Test,Integer> {


    @Query(value = "SELECT * FROM tests WHERE lesson_id = :lessonId", nativeQuery = true)
    List<Test> findTestsByLessonId(@Param("lessonId") Integer lessonId);

    @Query(value = "SELECT * FROM tests WHERE chapter_id = :chapterId AND lesson_id IS NULL", nativeQuery = true)
    Test findChapterTestByChapterId(@Param("chapterId") Integer chapterId);
}
