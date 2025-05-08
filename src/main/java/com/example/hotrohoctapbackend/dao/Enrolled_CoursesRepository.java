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
public interface Enrolled_CoursesRepository extends JpaRepository<Enrolled_Courses, Integer> {
    //    @Query(value = "SELECT * FROM enrolled_courses WHERE account_id = :userId AND course_id = :courseId", nativeQuery = true)
    @Query(value = "SELECT ec.id, ec.enrollment_date, ec.status, ec.account_id, ec.course_id FROM enrolled_courses ec WHERE ec.account_id = :userId AND ec.course_id = :courseId", nativeQuery = true)
    Optional<Enrolled_Courses> findEnrolledCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query(value = "SELECT * FROM enrolled_courses WHERE account_id = :accountId AND course_id = :courseId LIMIT 1", nativeQuery = true)
    Optional<Enrolled_Courses> findByAccountIdAndCourseId(@Param("accountId") Integer accountId, @Param("courseId") Integer courseId);

    List<Enrolled_Courses> findEnrolled_CoursesByCourseId(Integer courseId);

    List<Enrolled_Courses> findByCourseId(int courseId);

    @Query("SELECT ec FROM Enrolled_Courses ec " +
            "JOIN ec.account a " +
            "JOIN a.role r " +
            "WHERE ec.course.id = :courseId " +
            "AND a.fullname LIKE %:searchTerm% " +
            "AND r.roleName IN :roles")
    Page<Enrolled_Courses> findStudentsByCourseIdAndSearchTermAndRole(
            int courseId,
            String searchTerm,
            List<String> roles,
            Pageable pageable
    );

    List<Enrolled_Courses> findByAccountId(Integer accountId);
    

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

    @Query(value = "SELECT DISTINCT ec.account_id " +
            "FROM enrolled_courses ec " +
            "JOIN courses c ON ec.course_id = c.id " +
            "WHERE c.account_id = :accountId " +
            "AND ec.account_id != :accountId", nativeQuery = true)
    List<String> findEnrolledAccountsByCourseOwner(@Param("accountId") Long accountId);

    @Query(value = "SELECT DISTINCT c.account_id " +
            "FROM courses c " +
            "JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "WHERE ec.account_id = :accountId",
            nativeQuery = true)
    List<String> findCourseAuthorsByAccountId(@Param("accountId") Long accountId);

    List<Enrolled_Courses> findByAccountIdAndStatus(Integer accountId, String status);
}
