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
}
