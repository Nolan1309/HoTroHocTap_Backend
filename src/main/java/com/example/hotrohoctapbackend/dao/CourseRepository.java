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

import java.math.BigDecimal;
import java.util.List;
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
            "where c.is_deleted = 0 " +
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

    @Query(value = "SELECT c.id, c.duration, c.image_url, c.courses_title, e.enrollment_date, c.status  " +
            "FROM enrolled_courses e " +
            "JOIN courses c ON e.course_id = c.id " +
            "WHERE e.account_id = :accountId and c.is_deleted = 0 ",
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

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.cost , c.status, c.is_deleted, " +
            "cat.name AS category_name " + // Thêm trường category_name
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN course_categories cat ON c.course_category_id = cat.id " + // Thêm LEFT JOIN với bảng course_categories
            "WHERE c.account_id = :accountId",
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "JOIN account a ON c.account_id = a.id " +
                    "LEFT JOIN course_categories cat ON c.course_category_id = cat.id " + // Cập nhật countQuery tương tự
                    "WHERE c.account_id = :accountId",
            nativeQuery = true)
    Page<Object[]> findCoursesByAccountIdAdmin(@Param("accountId") int accountId, Pageable pageable);

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.cost, c.status, c.is_deleted, " +
            "cat.name AS category_name " + // Thêm category_name vào SELECT
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN course_categories cat ON c.course_category_id = cat.id", // Thêm LEFT JOIN với bảng course_categories
            countQuery = "SELECT COUNT(*) " +
                    "FROM courses c " +
                    "JOIN account a ON c.account_id = a.id " +
                    "LEFT JOIN course_categories cat ON c.course_category_id = cat.id", // Thêm LEFT JOIN vào countQuery
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

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.cost, c.status, c.is_deleted, " +
            "cat.name AS category_name " + // Thêm category_name vào SELECT
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN course_categories cat ON c.course_category_id = cat.id", // Thêm LEFT JOIN với bảng course_categories
            nativeQuery = true)
    List<Object[]> findAllCoursesResultList();

    @Query(value = "SELECT c.id, c.courses_title AS course_title, c.duration, c.price, c.status, c.is_deleted, " +
            "cat.name AS category_name " +  // Thêm trường category_name
            "FROM courses c " +
            "JOIN account a ON c.account_id = a.id " +
            "LEFT JOIN course_categories cat ON c.course_category_id = cat.id " +  // LEFT JOIN với bảng course_categories
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
                    v.isviewtest as viewTest 
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
            "GROUP BY l.course_id " +
            "HAVING total_lessons = total_videos AND total_lessons = total_tests", nativeQuery = true)
    List<Object[]> checkCourseCompleteness(@Param("courseId") Long courseId);

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
}
