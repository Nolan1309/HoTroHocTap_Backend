package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCourseDTORestoreList;
import com.example.hotrohoctapbackend.DTO.CourseDetailDTO;
import com.example.hotrohoctapbackend.entity.Category;
import com.example.hotrohoctapbackend.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RepositoryRestResource(path = "courses")
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query(value = "SELECT \n" +
            "    c.id AS course_id, \n" +
            "    c.course_category_id, \n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title AS course_title, \n" +
            "    c.type, \n" +
            "    c.status, \n" +
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
            "    c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, c.type, c.status \n" +
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

    @Query(value = "SELECT \n" +
            "    c.id AS course_id, \n" +
            "    c.course_category_id,  -- Tên danh mục cấp 3\n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title AS course_title, \n" +
            "    COUNT(DISTINCT ec.account_id) AS number_of_students, \n" +
            "    COUNT(DISTINCT l.id) AS total_lessons, \n" +
            "    COALESCE(AVG(cr.rating), 0) AS average_rating, \n" +
            "    c.author, \n" +
            "    c.course_output, \n" +
            "    c.created_at, \n" +
            "    c.updated_at, \n" +
            "    c.description, \n" +
            "    c.duration, \n" +
            "    c.language, \n" +
            "    c.status, \n" +
            "    c.type \n" +
            "FROM courses c \n" +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id \n" +
            "LEFT JOIN chapters ch ON c.id = ch.course_id \n" +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id \n" +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id \n" +
            "LEFT JOIN categories cat3 ON cat3.id_category = c.course_category_id  \n" +
            "LEFT JOIN categories cat2 ON cat2.id_category = cat3.parent_id  \n" +
            "LEFT JOIN categories cat1 ON cat1.id_category = cat2.parent_id  \n" +
            "WHERE \n" +
            "    (:categoryIds IS NULL OR :categoryIds = '' AND cat1.id_category = :categoryId) \n" +
            "    AND c.is_deleted = 0 \n" +
            "    AND c.status = 1\n" +
            "GROUP BY \n" +
            "    c.id, \n" +
            "    cat3.name, \n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title, \n" +
            "    c.author, \n" +
            "    c.course_output, \n" +
            "    c.created_at, \n" +
            "    c.updated_at, \n" +
            "    c.description, \n" +
            "    c.duration, \n" +
            "    c.language, \n" +
            "    c.status, \n" +
            "    c.type;",
            countQuery = "SELECT COUNT(c.id) \n" +
                    "FROM courses c \n" +
                    "LEFT JOIN categories cat3 ON cat3.id_category = c.course_category_id \n" +
                    "LEFT JOIN categories cat2 ON cat2.id_category = cat3.parent_id \n" +
                    "LEFT JOIN categories cat1 ON cat1.id_category = cat2.parent_id \n" +
                    "WHERE \n" +
                    "    (:categoryIds IS NULL OR :categoryIds = '' AND cat1.id_category = :categoryId) \n" +
                    "    AND c.is_deleted = 0 AND c.status = 1",  // Same filtering condition for deletion status
            nativeQuery = true)
    Page<Object[]> findByCourseCategoryIdsNoWithCap2(@Param("categoryId") Integer categoryId, @Param("categoryIds") Integer categoryIds, Pageable pageable);

    @Query(value = "SELECT \n" +
            "    c.id AS course_id, \n" +
            "    c.course_category_id,  -- Tên danh mục cấp 3\n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title AS course_title, \n" +
            "    COUNT(DISTINCT ec.account_id) AS number_of_students, \n" +
            "    COUNT(DISTINCT l.id) AS total_lessons, \n" +
            "    COALESCE(AVG(cr.rating), 0) AS average_rating, \n" +
            "    c.author, \n" +
            "    c.course_output, \n" +
            "    c.created_at, \n" +
            "    c.updated_at, \n" +
            "    c.description, \n" +
            "    c.duration, \n" +
            "    c.language, \n" +
            "    c.status, \n" +
            "    c.type \n" +
            "FROM courses c \n" +
            "LEFT JOIN enrolled_courses ec ON c.id = ec.course_id \n" +
            "LEFT JOIN chapters ch ON c.id = ch.course_id \n" +
            "LEFT JOIN lessons l ON ch.id = l.chapter_id \n" +
            "LEFT JOIN course_reviews cr ON c.id = cr.course_id \n" +
            "LEFT JOIN categories cat3 ON cat3.id_category = c.course_category_id  \n" +
            "LEFT JOIN categories cat2 ON cat2.id_category = cat3.parent_id  \n" +
            "LEFT JOIN categories cat1 ON cat1.id_category = cat2.parent_id  \n" +
            "WHERE \n" +
            "    (:categoryIds IS NOT NULL AND cat2.id_category = :categoryIds AND cat1.id_category = :categoryId) \n" +
            "    AND c.is_deleted = 0 \n" +
            "    AND c.status = 1\n" +
            "GROUP BY \n" +
            "    c.id, \n" +
            "    cat3.name, \n" +
            "    c.image_url, \n" +
            "    c.price, \n" +
            "    c.cost, \n" +
            "    c.courses_title, \n" +
            "    c.author, \n" +
            "    c.course_output, \n" +
            "    c.created_at, \n" +
            "    c.updated_at, \n" +
            "    c.description, \n" +
            "    c.duration, \n" +
            "    c.language, \n" +
            "    c.status, \n" +
            "    c.type;",
            countQuery = "SELECT COUNT(c.id) \n" +
                    "FROM courses c \n" +
                    "LEFT JOIN categories cat3 ON cat3.id_category = c.course_category_id \n" +
                    "LEFT JOIN categories cat2 ON cat2.id_category = cat3.parent_id \n" +
                    "LEFT JOIN categories cat1 ON cat1.id_category = cat2.parent_id \n" +
                    "WHERE \n" +
                    "    (:categoryIds IS NOT NULL AND cat2.id_category = :categoryIds AND cat1.id_category = :categoryId) \n" +
                    "    AND c.is_deleted = 0 AND c.status = 1",  // Same filtering condition for deletion status
            nativeQuery = true)
    Page<Object[]> findByCourseCategoryIdsWithCap2(@Param("categoryId") Integer categoryId, @Param("categoryIds") Integer categoryIds, Pageable pageable);


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
            "where c.is_deleted = 0 and c.status = 1 " +
            "GROUP BY c.id, c.course_category_id, c.image_url, c.price, c.cost, c.courses_title, " +
            "c.author, c.course_output, c.created_at, c.updated_at, c.description, c.duration, " +
            "c.language, c.status, c.type",
            countQuery = "SELECT COUNT(c.id) FROM courses c where c.is_deleted = 0 and c.status = 1 ",
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

    @Query(value = "SELECT c.id, c.duration, c.image_url, c.courses_title, e.enrollment_date, c.status, c.is_deleted  " +
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

    @Query(value = "SELECT \n" +
            "    c.id AS course_id,\n" +
            "    c.courses_title AS course_title,\n" +
            "    c.description,\n" +
            "    c.image_url,\n" +
            "    c.course_output,\n" +
            "    c.language,\n" +
            "    c.author,\n" +
            "    c.duration,\n" +
            "    c.cost,\n" +
            "    c.price,\n" +
            "    c.created_at,\n" +
            "    c.updated_at,\n" +
            "    c.status,\n" +
            "    c.type,\n" +
            "    c.deleted_date,\n" +
            "    c.is_deleted,\n" +
            "    c.account_id,\n" +
            "    cat1.name AS category_name_level1,\n" +
            "    cat1.id_category AS category_id_level1,\n" +
            "    cat2.name AS category_name_level2,\n" +
            "    cat2.id_category AS category_id_level2,\n" +
            "    cat3.name AS category_name_level3,\n" +
            "    cat3.id_category AS category_id_level3\n" +
            "FROM \n" +
            "    courses c\n" +
            "JOIN \n" +
            "    account a ON c.account_id = a.id\n" +
            "LEFT JOIN \n" +
            "    categories cat3 ON c.course_category_id = cat3.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat2 ON cat3.parent_id = cat2.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat1 ON cat2.parent_id = cat1.id_category\n" +
            "WHERE \n" +
            "    c.account_id = :accountId \n" +
            "    AND c.is_deleted = 0;\n ",
            countQuery = "SELECT \n" +
                    "    COUNT(*) AS total_courses\n" +
                    "FROM \n" +
                    "    courses c\n" +
                    "JOIN \n" +
                    "    account a ON c.account_id = a.id\n" +
                    "WHERE \n" +
                    "    c.account_id = 1 \n" +
                    "    AND c.is_deleted = 0 ",
            nativeQuery = true)
    Page<Object[]> findCoursesByAccountIdAdmin(@Param("accountId") int accountId, Pageable pageable);

    @Query(value = "SELECT \n" +
            "    c.id AS course_id,\n" +
            "    c.courses_title AS course_title,\n" +
            "    c.description,\n" +
            "    c.image_url,\n" +
            "    c.course_output,\n" +
            "    c.language,\n" +
            "    c.author,\n" +
            "    c.duration,\n" +
            "    c.cost,\n" +
            "    c.price,\n" +
            "    c.created_at,\n" +
            "    c.updated_at,\n" +
            "    c.status,\n" +
            "    c.type,\n" +
            "    c.deleted_date,\n" +
            "    c.is_deleted,\n" +
            "    c.account_id,\n" +
            "    cat3.name AS category_name_level3,\n" +
            "    cat3.id_category AS category_id_level3,\n" +
            "    cat2.name AS category_name_level2,\n" +
            "    cat2.id_category AS category_id_level2,\n" +
            "    cat1.name AS category_name_level1,\n" +
            "    cat1.id_category AS category_id_level1\n" +
            "FROM \n" +
            "    courses c\n" +
            "JOIN \n" +
            "    account a ON c.account_id = a.id\n" +
            "LEFT JOIN \n" +
            "    categories cat3 ON c.course_category_id = cat3.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat2 ON cat3.parent_id = cat2.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat1 ON cat2.parent_id = cat1.id_category\n" +
            "WHERE \n" +
            "    c.is_deleted = 0\n", // Thêm LEFT JOIN với bảng course_categories
            countQuery = "SELECT \n" +
                    "    COUNT(*) AS total_courses\n" +
                    "FROM \n" +
                    "    courses c\n" +
                    "JOIN \n" +
                    "    account a ON c.account_id = a.id\n" +
                    "LEFT JOIN \n" +
                    "    categories cat3 ON c.course_category_id = cat3.id_category\n" +
                    "LEFT JOIN \n" +
                    "    categories cat2 ON cat3.parent_id = cat2.id_category\n" +
                    "LEFT JOIN \n" +
                    "    categories cat1 ON cat2.parent_id = cat1.id_category\n" +
                    "WHERE \n" +
                    "    c.is_deleted = 0\n", // Thêm LEFT JOIN vào countQuery
            nativeQuery = true)
    Page<Object[]> findAllCoursesResult(Pageable pageable);

    @Query(value = """
            SELECT 
                c.id AS course_id,
                c.courses_title AS course_title,
                c.description,
                c.image_url,
                c.course_output,
                c.language,
                c.author,
                c.duration,
                c.cost,
                c.price,
                c.created_at,
                c.updated_at,
                c.status,
                c.type,
                c.deleted_date,
                c.is_deleted,
                c.account_id,
                cat3.name AS category_name_level3,
                cat3.id_category AS category_id_level3,
                cat2.name AS category_name_level2,
                cat2.id_category AS category_id_level2,
                cat1.name AS category_name_level1,
                cat1.id_category AS category_id_level1,
                COUNT(DISTINCT ec.account_id) AS student_count
            FROM 
                courses c
            JOIN 
                account a ON c.account_id = a.id
            LEFT JOIN 
                categories cat3 ON c.course_category_id = cat3.id_category
            LEFT JOIN 
                categories cat2 ON cat3.parent_id = cat2.id_category
            LEFT JOIN 
                categories cat1 ON cat2.parent_id = cat1.id_category
            LEFT JOIN 
                enrolled_courses ec ON c.id = ec.course_id
            WHERE 
                c.is_deleted = 0
                AND (:categoryId1 IS NULL OR cat1.id_category = :categoryId1)
                AND (:categoryId2 IS NULL OR cat2.id_category = :categoryId2)
                AND (:categoryId3 IS NULL OR cat3.id_category = :categoryId3)
                AND (:searchTerm IS NULL OR c.courses_title LIKE CONCAT('%', :searchTerm, '%') OR c.author LIKE CONCAT('%', :searchTerm, '%'))
            GROUP BY
                c.id, c.courses_title, c.description, c.image_url, c.course_output, c.language,
                c.author, c.duration, c.cost, c.price, c.created_at, c.updated_at, c.status,
                c.type, c.deleted_date, c.is_deleted, c.account_id, cat3.name, cat3.id_category,
                cat2.name, cat2.id_category, cat1.name, cat1.id_category
            """,
            countQuery = """
                    SELECT 
                        COUNT(*) AS total_courses
                    FROM 
                        courses c
                    JOIN 
                        account a ON c.account_id = a.id
                    LEFT JOIN 
                        categories cat3 ON c.course_category_id = cat3.id_category
                    LEFT JOIN 
                        categories cat2 ON cat3.parent_id = cat2.id_category
                    LEFT JOIN 
                        categories cat1 ON cat2.parent_id = cat1.id_category
                    WHERE 
                        c.is_deleted = 0
                        AND (:categoryId1 IS NULL OR cat1.id_category = :categoryId1)
                        AND (:categoryId2 IS NULL OR cat2.id_category = :categoryId2)
                        AND (:categoryId3 IS NULL OR cat3.id_category = :categoryId3)
                       AND (:searchTerm IS NULL OR c.courses_title LIKE CONCAT('%', :searchTerm, '%') OR c.author LIKE CONCAT('%', :searchTerm, '%'))
                    """,
            nativeQuery = true)
    Page<Object[]> findAllCoursesResultSearch(@Param("categoryId1") Integer categoryId1,
                                              @Param("categoryId2") Integer categoryId2,
                                              @Param("categoryId3") Integer categoryId3,
                                              @Param("searchTerm") String searchTerm,
                                              Pageable pageable);

    @Query("SELECT c FROM Course c " +
            "LEFT JOIN c.category " +
            "LEFT JOIN c.account " +
            "WHERE (:categoryId IS NULL OR c.category.id = :categoryId) " +
            "AND (:searchTerm IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND c.isDeleted = false")
    Page<Course> findCoursesAdmin(@Param("categoryId") Integer categoryId,
                                  @Param("searchTerm") String searchTerm,
                                  Pageable pageable);

    @Query(value = "SELECT \n" +
            "    c.id AS course_id,\n" +
            "    c.courses_title AS course_title,\n" +
            "    c.description,\n" +
            "    c.image_url,\n" +
            "    c.course_output,\n" +
            "    c.language,\n" +
            "    c.author,\n" +
            "    c.duration,\n" +
            "    c.cost,\n" +
            "    c.price,\n" +
            "    c.created_at,\n" +
            "    c.updated_at,\n" +
            "    c.status,\n" +
            "    c.type,\n" +
            "    c.deleted_date,\n" +
            "    c.is_deleted,\n" +
            "    c.account_id,\n" +
            "    cat3.name AS category_name_level3,\n" +
            "    cat3.id_category AS category_id_level3,\n" +
            "    cat2.name AS category_name_level2,\n" +
            "    cat2.id_category AS category_id_level2,\n" +
            "    cat1.name AS category_name_level1,\n" +
            "    cat1.id_category AS category_id_level1\n" +
            "FROM \n" +
            "    courses c\n" +
            "JOIN \n" +
            "    account a ON c.account_id = a.id\n" +
            "LEFT JOIN \n" +
            "    categories cat3 ON c.course_category_id = cat3.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat2 ON cat3.parent_id = cat2.id_category\n" +
            "LEFT JOIN \n" +
            "    categories cat1 ON cat2.parent_id = cat1.id_category\n" +
            "WHERE \n" +
            "    c.is_deleted = 0\n", // Thêm LEFT JOIN với bảng course_categories
            nativeQuery = true)
    List<Object[]> findAllCoursesResult();

    @Query(value = "SELECT c.id, c.courses_title, c.duration, c.price, c.cost " +
            "FROM courses c " +
            "WHERE c.is_deleted = 0 ",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "WHERE c.is_deleted = 0 ",
            nativeQuery = true)
    Page<Object[]> getCourseofDiscount(Pageable pageable);

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.cost, c.status, c.is_deleted, " +
            "cat.name AS category_name, c.account_id, c.course_category_id " + // Thêm category_name vào SELECT
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN categories cat ON c.course_category_id = cat.id_category", // Thêm LEFT JOIN với bảng course_categories
            nativeQuery = true)
    List<Object[]> findAllCoursesResultList();

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.status, c.is_deleted, " +
            "cat.name AS category_name " +  // Thêm trường category_name
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN categories cat ON c.course_category_id = cat.id_category " +  // LEFT JOIN với bảng course_categories
            "WHERE c.account_id = :accountId",
            nativeQuery = true)
    List<Object[]> findCoursesByAccountIdListAdmin(@Param("accountId") int accountId);

    @Query(value = """
                SELECT 
                    c.id AS chapterId,
                    c.chapter_title AS chapterTitle,
                    COUNT(l.id) AS lessonCount
                FROM chapters c
                LEFT JOIN lessons l ON c.id = l.chapter_id
                WHERE c.course_id = :courseId
                GROUP BY c.id
                ORDER BY c.id
            """, nativeQuery = true)
    List<Object[]> findChaptersByCourseId(@Param("courseId") Long courseId);

    @Query(value = """
                SELECT 
                    v.id AS videoId,
                    v.video_title AS videoTitle,
                    v.duration AS videoDuration,
                    v.isviewtest as viewTest,
                    v.url as linkVideo
                FROM videos v
                LEFT JOIN lessons l ON v.lesson_id = l.id
                WHERE l.chapter_id = :chapterId
                ORDER BY v.id
            """, nativeQuery = true)
    List<Object[]> findVideosByChapterId(@Param("chapterId") Integer chapterId);

    @Query(value = """
                WITH RevenueData AS (
                                         SELECT\s
                                             pd.course_id,
                                             SUM(pd.price) AS total_revenue
                                         FROM payments_detail pd
                                         GROUP BY pd.course_id
                                     ),
                                     StudentsData AS (
                                         SELECT\s
                                             ec.course_id,
                                             COUNT(DISTINCT ec.account_id) AS total_students
                                         FROM enrolled_courses ec
                                         GROUP BY ec.course_id
                                     )
                                     SELECT\s
                                         c.courses_title AS courseName,
                                         sd.total_students AS students,
                                         rd.total_revenue AS revenue,
                                         c.status AS status, 
                                         a.fullname AS authorName
                                     FROM courses c
                                     LEFT JOIN StudentsData sd ON c.id = sd.course_id
                                     LEFT JOIN RevenueData rd ON c.id = rd.course_id
                                     INNER JOIN account a ON c.account_id = a.id
                                     WHERE c.is_deleted = 0
                                     ORDER BY rd.total_revenue DESC;
                                     
            """, nativeQuery = true)
    List<Object[]> getCourseReport();

    @Query(value = "SELECT l.course_id, " +
            "COUNT(DISTINCT l.id) AS total_lessons, " +
            "COUNT(DISTINCT v.id) AS total_videos, " +
            "COUNT(DISTINCT t.id) AS total_tests " +
            "FROM lessons l " +
            "LEFT JOIN videos v ON l.id = v.lesson_id " +
            "LEFT JOIN tests t ON l.id = t.lesson_id AND t.is_summary = 0 " +
            "WHERE l.course_id = :courseId " +
            "GROUP BY l.course_id", nativeQuery = true)
    List<Object[]> checkCourseCompleteness(@Param("courseId") Integer courseId);

    @Query(value = "SELECT status FROM courses WHERE id = :courseId", nativeQuery = true)
    Optional<Boolean> findStatusByCourseId(@Param("courseId") Long courseId);

    @Query(value = "SELECT cat.id, cat.name AS category_name " +
            "FROM courses c " +
            "JOIN course_categories cat ON c.course_category_id = cat.id " +
            "WHERE c.is_deleted = 0 AND cat.is_deleted = 0 AND c.id = :courseId " +
            "LIMIT 1", nativeQuery = true)
    List<Object[]> findCategoryByCourseId(@Param("courseId") Integer courseId);

    @Query(value = "SELECT d.discount_value " +
            "FROM course_discounts cd " +
            "JOIN discounts d ON cd.discount_id = d.id " +
            "JOIN courses c ON cd.course_id = c.id " +
            "WHERE cd.course_id = :courseId " +
            "AND cd.is_deleted = false " +
            "AND d.is_deleted = false " +
            "AND CURRENT_DATE BETWEEN d.start_date AND d.end_date " +
            "AND cd.is_check = true " +
            "LIMIT 1", nativeQuery = true)
    Double getDiscountForCourse(@Param("courseId") Integer courseId);

    List<Course> findByIsDeletedTrue();

    @Query(value = """
            SELECT 
                c.id, 
                c.author, 
                c.cost, 
                c.course_output, 
                c.created_at, 
                c.deleted_date, 
                c.description, 
                c.duration, 
                c.image_url, 
                c.is_deleted, 
                c.language, 
                c.price, 
                c.status, 
                c.courses_title, 
                c.type, 
                c.updated_at, 
                c.course_category_id, 
                c.account_id
            FROM courses c
            WHERE c.is_deleted = 1
            """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM courses c
                    WHERE c.is_deleted = 1
                    """,
            nativeQuery = true)
    Page<Object[]> findDeletedCourses(Pageable pageable);

    // Tìm kiếm theo fullname
    @Query(value = """
            SELECT c.id, 
                   c.author, 
                   c.cost, 
                   c.course_output, 
                   c.created_at, 
                   c.deleted_date, 
                   c.description, 
                   c.duration, 
                   c.image_url, 
                   c.is_deleted, 
                   c.language, 
                   c.price, 
                   c.status, 
                   c.courses_title, 
                   c.type, 
                   c.updated_at, 
                   c.course_category_id, 
                   c.account_id
            FROM courses c
            WHERE c.is_deleted = 1 
            AND c.courses_title LIKE CONCAT('%', :courseTitle, '%')
            ORDER BY c.created_at DESC
            """, nativeQuery = true)
    Page<Object[]> searchCoursesByTitle(String courseTitle, Pageable pageable);

    // Tìm kiếm theo ngày xóa
    @Query(value = """
            SELECT c.id, 
                   c.author, 
                   c.cost, 
                   c.course_output, 
                   c.created_at, 
                   c.deleted_date, 
                   c.description, 
                   c.duration, 
                   c.image_url, 
                   c.is_deleted, 
                   c.language, 
                   c.price, 
                   c.status, 
                   c.courses_title, 
                   c.type, 
                   c.updated_at, 
                   c.course_category_id, 
                   c.account_id
            FROM courses c
            WHERE c.is_deleted = 1 
            AND DATE(c.deleted_date) = :deletedDate
            ORDER BY c.created_at DESC
            """, nativeQuery = true)
    Page<Object[]> searchCoursesByDeletedDate(String deletedDate, Pageable pageable);

    // Tìm kiếm theo cả fullname và deletedDate
    @Query(value = """
            SELECT c.id, 
                   c.author, 
                   c.cost, 
                   c.course_output, 
                   c.created_at, 
                   c.deleted_date, 
                   c.description, 
                   c.duration, 
                   c.image_url, 
                   c.is_deleted, 
                   c.language, 
                   c.price, 
                   c.status, 
                   c.courses_title, 
                   c.type, 
                   c.updated_at, 
                   c.course_category_id, 
                   c.account_id
            FROM courses c
            WHERE c.is_deleted = 1 
            AND c.courses_title LIKE CONCAT('%', :courseTitle, '%')
            AND (DATE(c.deleted_date) = :deletedDate OR :deletedDate IS NULL)
            ORDER BY c.created_at DESC
            """, nativeQuery = true)
    Page<Object[]> searchCoursesByCourseTitleAndDeleteDate(String courseTitle, String deletedDate, Pageable pageable);

    @Query(value = """
            SELECT 
                c.id, 
                c.author, 
                c.cost, 
                c.course_output, 
                c.created_at, 
                c.deleted_date, 
                c.description, 
                c.duration, 
                c.image_url, 
                c.is_deleted, 
                c.language, 
                c.price, 
                c.status, 
                c.courses_title, 
                c.type, 
                c.updated_at, 
                c.course_category_id, 
                c.account_id
            FROM courses c
            WHERE c.is_deleted = 0
            """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM courses c
                    WHERE c.is_deleted = 0
                    """,
            nativeQuery = true)
    List<Object[]> findNoDeletedCoursesList();

    @Query(value = """
             SELECT 
                 c.id, 
                 c.author, 
                 c.cost, 
                 c.course_output, 
                 c.created_at, 
                 c.deleted_date, 
                 c.description, 
                 c.duration, 
                 c.is_deleted, 
                 c.language, 
                 c.price, 
                 c.status, 
                 c.courses_title, 
                 c.type, 
                 c.updated_at, 
                 c.course_category_id, 
                 c.account_id
             FROM courses c
            WHERE c.is_deleted = 0
                 AND (:author IS NULL OR LOWER(c.author) LIKE LOWER(CONCAT('%', :author, '%')))
                 AND (:title IS NULL OR LOWER(c.courses_title) LIKE LOWER(CONCAT('%', :title, '%')))
                 AND (:language IS NULL OR LOWER(c.language) LIKE LOWER(CONCAT('%', :language, '%')))
                 AND (:price IS NULL OR LOWER(c.price) LIKE LOWER(CONCAT('%', :price, '%')))
                 AND (:type IS NULL OR LOWER(c.type) LIKE LOWER(CONCAT('%', :type, '%')))
             """,
            nativeQuery = true)
    List<Object[]> findCoursesListQuery(@Param("author") String author,
                                        @Param("title") String title,
                                        @Param("language") String language,
                                        @Param("price") String price,
                                        @Param("type") String type);

    // Khóa học phổ biến, lọc theo tên (title có thể là NULL)
    @Query("SELECT c FROM Course c " +
            "LEFT JOIN c.enrolledCourses e " +
            "WHERE (:title IS NULL OR c.title LIKE %:title%) AND " +
            "c.status = true AND " +
            "c.isDeleted = false " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(e) DESC")
    Page<Course> findPopularCourses(String title, Pageable pageable);

    // Khóa học giảm giá, lọc theo tên khóa học, price < cost (title có thể là NULL)
    @Query("SELECT c FROM Course c WHERE (:title IS NULL OR c.title LIKE %:title%) " +
            "AND c.status = true " +
            "AND c.price < c.cost " +
            "AND c.isDeleted = false " +
            "ORDER BY (c.cost - c.price) DESC")
    Page<Course> findDiscountCourses(String title, Pageable pageable);


    @Query("SELECT c FROM Course c WHERE " +
            "(:title IS NULL OR c.title LIKE %:title%) AND " +
            "(:categoryId IS NULL OR c.category.id = :categoryId) AND " +
            "c.status = true AND " +
            "c.isDeleted = false")
    Page<Course> findByTitleAndCategory(String title, Integer categoryId, Pageable pageable);
}
