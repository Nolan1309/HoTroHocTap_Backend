package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "SELECT \n" +
            "    c.id AS course_id, \n" +
            "    c.course_category_id, \n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title AS course_title, \n" +
            "    COUNT(ec.account_id) AS number_of_students, \n" +
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
            "LEFT JOIN \n" +
            "    course_reviews cr ON c.id = cr.course_id \n" +
            "GROUP BY \n" +
            "    c.id, c.course_category_id, c.image_url, c.price, c.courses_title \n" +
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
            "c.status " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "WHERE c.course_category_id = :courseCategoryId " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status",
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
            "c.status " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "WHERE c.course_category_id IN :courseCategoryIds " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status",
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
            "c.status " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status",
            countQuery = "SELECT COUNT(c.id) FROM courses c",
            nativeQuery = true)
    Page<Object[]> findAllCourses(Pageable pageable);


}
