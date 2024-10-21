package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "enrolled_courses")
public interface Enrolled_CoursesRepository extends JpaRepository<Enrolled_Courses,Integer> {
    @Query(value = "SELECT * FROM enrolled_courses WHERE account_id = :userId AND course_id = :courseId", nativeQuery = true)
    Optional<Enrolled_Courses> findEnrolledCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);


    List<Enrolled_Courses> findByAccountId(Integer accountId);

//    @Query(value = "SELECT c.id, c.duration, c.image_url, c.courses_title, e.enrollment_date " +
//            "FROM enrolled_courses e " +
//            "JOIN courses c ON e.course_id = c.id " +
//            "WHERE e.account_id = :accountId",
//            countQuery = "SELECT COUNT(*) FROM enrolled_courses e WHERE e.account_id = :accountId",
//            nativeQuery = true)
//    Page<Object[]> findCoursesByAccountIdNative(@Param("accountId") Integer accountId, Pageable pageable);
}
