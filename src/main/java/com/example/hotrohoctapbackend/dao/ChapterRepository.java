package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "chapters")
public interface ChapterRepository extends JpaRepository<Chapter,Integer> {
//    @Query(value = "SELECT * FROM chapters WHERE course_id  = :course_id ", nativeQuery = true)
//    List<Chapter> findByCourseId(@Param("course_id") int course_id );
    List<Chapter> findByCourseId(Integer courseId);
}
