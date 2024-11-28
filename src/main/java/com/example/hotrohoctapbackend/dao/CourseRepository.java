package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "SELECT \n" +
            "    c.id AS course_id, \n" +
            "    c.course_category_id, \n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title AS course_title, \n" +
            "    c.type, \n" +
            "    COUNT(DISTINCT ec.account_id) AS number_of_students, \n" +
            "    COUNT(DISTINCT l.id) AS total_lessons, \n" +
            "    COALESCE(AVG(cr.rating), 0) AS average_rating \n" +
            "FROM \n" +
            "    courses c \n" +
            "LEFT JOIN \n" +
            "    enrolled_courses ec ON c.id = ec.course_id \n" +
            "LEFT JOIN \n" +
            "    chapters ch ON c.id = ch.course_id \n" +
            "LEFT JOIN \n" +
            "    lessons l ON ch.id = l.chapter_id \n" +
            "LEFT JOIN\n" +
            "    course_reviews cr ON c.id = cr.course_id \n" +
            "GROUP BY \n" +
            "    c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, c.type \n" +
            "ORDER BY \n" +
            "    number_of_students DESC \n" +
            "LIMIT 6;\n",
            nativeQuery = true)
    List<Object[]> findTopCoursesWithDetails();

    @Query(value = "SELECT " +
            "c.id AS course_id, " +
            "c.course_category_id, " +
            "c.image_url, " +
            "c.price, " +
            "c.cost, " +
            "c.courses_title AS course_title, " +
            "COUNT(DISTINCT ec.account_id) AS number_of_students, " +
            "COUNT(DISTINCT l.id) AS total_lessons, " +
            "COALESCE(AVG(cr.rating), 0) AS average_rating, " +
            "c.author, " +
            "c.course_output, " +
            "c.created_at, " +
            "c.updated_at, " +
            "c.description, " +
            "c.duration, " +
            "c.language, " +
            "c.status ," +
            "c.type " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "WHERE c.course_category_id = :courseCategoryId " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status, c.type",
            countQuery = "SELECT COUNT(c.id) FROM courses c WHERE c.course_category_id = :courseCategoryId",
            nativeQuery = true)
    Page<Object[]> findByCourseCategoryId(@Param("courseCategoryId") Integer courseCategoryId, Pageable pageable);

    @Query(value = "SELECT " +
            "c.id AS course_id, " +
            "c.course_category_id, " +
            "c.image_url, " +
            "c.price, " +
            "c.cost, " +
            "c.courses_title AS course_title, " +
            "COUNT(DISTINCT ec.account_id) AS number_of_students, " +
            "COUNT(DISTINCT l.id) AS total_lessons, " +
            "COALESCE(AVG(cr.rating), 0) AS average_rating, " +
            "c.author, " +
            "c.course_output, " +
            "c.created_at, " +
            "c.updated_at, " +
            "c.description, " +
            "c.duration, " +
            "c.language, " +
            "c.status, " +
            "c.type " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "WHERE c.course_category_id IN :courseCategoryIds " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status, c.type",
            countQuery = "SELECT COUNT(c.id) FROM courses c WHERE c.course_category_id IN :courseCategoryIds",
            nativeQuery = true)
    Page<Object[]> findByCourseCategoryIds(@Param("courseCategoryIds") List<Integer> courseCategoryIds, Pageable pageable);


    @Query(value = "SELECT " +
            "c.id AS course_id, " +
            "c.course_category_id, " +
            "c.image_url, " +
            "c.price, " +
            "c.cost, " +
            "c.courses_title AS course_title, " +
            "COUNT(DISTINCT ec.account_id) AS number_of_students, " +
            "COUNT(DISTINCT l.id) AS total_lessons, " +
            "COALESCE(AVG(cr.rating), 0) AS average_rating, " +
            "c.author, " +
            "c.course_output, " +
            "c.created_at, " +
            "c.updated_at, " +
            "c.description, " +
            "c.duration, " +
            "c.language, " +
            "c.status, " +
            "c.type " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status, c.type",
            countQuery = "SELECT COUNT(c.id) FROM courses c",
            nativeQuery = true)
    Page<Object[]> findAllCourses(Pageable pageable);

    @Query(value = "SELECT id, author, cost, course_output, created_at, description, duration, image_url, language, price, status, courses_title, updated_at, course_category_id, account_id " +
            "FROM courses WHERE id = :id", nativeQuery = true)
    List<Object[]> findCourseById(@Param("id") Integer id);


    @Query(value = "SELECT " +
            "(SELECT COUNT(*) FROM enrolled_courses WHERE course_id = :courseId) AS total_students, " +
            "(SELECT COUNT(l.id) FROM lessons l JOIN chapters c ON l.chapter_id = c.id WHERE c.course_id = :courseId) AS total_lessons",
            nativeQuery = true)
    List<Object[]> getCourseStatistics(@Param("courseId") Integer courseId);

    @Query("SELECT c.type FROM Course c WHERE c.id = :id")
    String findCourseTypeById(@Param("id") int id);

    @Query(value = "SELECT c.id, c.duration, c.image_url, c.courses_title, e.enrollment_date " +
            "FROM enrolled_courses e " +
            "JOIN courses c ON e.course_id = c.id " +
            "WHERE e.account_id = :accountId",
            countQuery = "SELECT COUNT(*) FROM enrolled_courses e WHERE e.account_id = :accountId",
            nativeQuery = true)
    Page<Object[]> findCoursesByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

//    @Query(value = """
//            SELECT
//                c.id,
//                ch.id AS chapter_id,
//                ch.chapter_title AS chapter_title,
//
//                l.id AS lesson_id,
//                l.lesson_title AS lesson_title,
//                l.duration AS lesson_duration,
//
//                v.id AS video_id,
//                v.document_short AS video_title,
//                v.url AS video_url,
//                v.document_short AS document_short,
//                v.document_url AS document_url,
//
//                t.id AS test_id,
//                t.title AS test_title,
//                CASE
//                    WHEN t.is_summary = 1 THEN 'Test Bài'
//                    ELSE 'Test Chương'
//                END AS test_type
//
//            FROM
//                courses c
//
//            JOIN chapters ch
//                ON c.id = ch.course_id
//
//            JOIN lessons l
//                ON ch.id = l.chapter_id
//
//            LEFT JOIN videos v
//                ON l.id = v.lesson_id
//
//            LEFT JOIN tests t
//                ON l.id = t.lesson_id OR ch.id = t.chapter_id
//
//            WHERE
//                c.id = :courseId
//            """, nativeQuery = true)
//    List<Object[]> findCourseDetails(@Param("courseId") Integer courseId);
    @Query(value = "SELECT " +
            "c.id AS course_id, " +
            "c.courses_title AS course_title, " +
            "c.duration AS course_duration, " +
            "c.language AS course_language, " +
            "cat.name AS category_name, " +
            "c.is_deleted AS deleted " +
            "FROM courses c " +
            "LEFT JOIN course_categories cat ON c.course_category_id = cat.id",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "LEFT JOIN course_categories cat ON c.course_category_id = cat.id",
            nativeQuery = true)
    Page<Object[]> findCourseWithCategory(Pageable pageable);
    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.status, c.is_deleted " +
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "WHERE c.account_id = :accountId",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "JOIN account a ON c.account_id = a.id " +
                    "WHERE c.account_id = :accountId",
            nativeQuery = true)
    Page<Object[]> findCoursesByAccountIdAdmin(@Param("accountId") int accountId, Pageable pageable);
    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.status, c.is_deleted " +
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "JOIN account a ON c.account_id = a.id",
            nativeQuery = true)
    Page<Object[]> findAllCoursesResult(Pageable pageable);
    @Query(value = "SELECT c.id, c.courses_title, c.duration, c.price, c.cost " +
            "FROM courses c " +
            "WHERE c.is_deleted = 0 ",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "WHERE c.is_deleted = 0 ",
            nativeQuery = true)
    Page<Object[]> getCourseofDiscount(Pageable pageable);
}
