package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "SELECT " +
            "c.id AS course_id, " +
            "c.id_category, " +
            "c.image_url, " +
            "c.price, " +
            "c.cost, " +
            "c.courses_title AS course_title, " +
            "COUNT(DISTINCT ec.account_id) AS number_of_students, " +
            "COUNT(DISTINCT l.id) AS total_lessons, " +
            "COALESCE(AVG(cr.rating), 0) AS average_rating " +
            "FROM courses c " +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id " +
            "LEFT JOIN chapters ch ON c.id = ch.course_id " +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id " +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id " +
            "GROUP BY c.id, c.id_category, c.image_url, c.price, c.courses_title " +
            "ORDER BY number_of_students DESC " +
            "LIMIT 6",
            nativeQuery = true)
    List<Object[]> findTopCoursesWithDetails();

}
