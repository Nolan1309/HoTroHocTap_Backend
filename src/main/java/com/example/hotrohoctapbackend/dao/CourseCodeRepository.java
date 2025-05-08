package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.CourseCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "course_code")
public interface CourseCodeRepository extends JpaRepository<CourseCode, Integer> {
    @Query(value = "SELECT id, code, created_at, expiry_date, status, used_at, account_id, course_id FROM course_code", nativeQuery = true)
    List<CourseCode> findAllCourseCodes();

    @Query("SELECT cc FROM CourseCode cc WHERE cc.code LIKE %:code% AND cc.course.id  = :courseId")
    Page<CourseCode> findByCodeContaining(Integer courseId, String code, Pageable pageable);

    Optional<CourseCode> findCourseCodeByCode(String code);

    Optional<CourseCode> findCourseCodeByCourseId(Integer courseId);
}
