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

    @Query(value = "SELECT DISTINCT a.id AS user_id,  a.fullname AS user_name,a.email as email " +
            "FROM enrolled_courses ec " +
            "INNER JOIN account a ON ec.account_id = a.id " +
            "WHERE (ec.status = 'Actived' OR ec.status = 'Studying') and a.is_deleted = 0 ",
            nativeQuery = true)
    List<Object[]> findActiveEnrolledUsers();
    @Query(value = "SELECT a.id AS account_id, a.email, a.phone, a.gender " +
            "FROM account a " +
            "JOIN enrolled_courses ec ON a.id = ec.account_id " +
            "JOIN courses c ON ec.course_id = c.id " +
            "WHERE c.id = :courseId AND ec.status = 'Studying'",
            countQuery = "SELECT COUNT(*) " +
                    "FROM account a " +
                    "JOIN enrolled_courses ec ON a.id = ec.account_id " +
                    "JOIN courses c ON ec.course_id = c.id " +
                    "WHERE c.id = :courseId AND ec.status = 'Studying'",
            nativeQuery = true)
    Page<Object[]> findAccountsByCourseIdAndStatus(@Param("courseId") int courseId, Pageable pageable);

    Optional<Enrolled_Courses> findByAccount_IdAndCourse_Id(Integer accountId, Integer courseId);
}
