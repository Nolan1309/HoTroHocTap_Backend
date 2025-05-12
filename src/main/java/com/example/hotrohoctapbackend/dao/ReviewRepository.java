package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review, Integer> {
//    @Query(value = "SELECT review.id, review.created_at, review.rating , review.review, review.updated_at , review.account_id , review.course_id , ac.fullname, ac.image  from course_reviews review\n" +
//            "inner join account ac on review.account_id = ac.id\n" +
//            "where review.course_id = :courseId", nativeQuery = true)
//    List<Object[]> findByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT r FROM Review r WHERE r.course.id = :courseId AND r.isDeleted = false AND r.status = 'APPROVED'")
    Page<Review> findByCourseId(@Param("courseId") Integer courseId, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.test.id = :testId AND r.isDeleted = false AND r.status = 'APPROVED'")
    Page<Review> findByTestId(@Param("testId") Integer testId, Pageable pageable);

    Review findByCourseIdAndAccountId(@Param("courseId") Integer courseId, @Param("accountId") Integer accountId);

    @Query("""
                SELECT r FROM Review r
                JOIN FETCH r.account a
                JOIN FETCH r.course c
                WHERE r.test IS NULL
                  AND (:keyword IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:courseId IS NULL OR c.id = :courseId)
                  AND (:status IS NULL OR LOWER(r.status) = LOWER(:status))
                  AND (:rating IS NULL OR r.rating = :rating)
            """)
    Page<Review> findCourseReviews(
            @Param("keyword") String keyword,
            @Param("courseId") Integer courseId,
            @Param("status") String status,
            @Param("rating") Integer rating,
            Pageable pageable
    );

    // Dành cho tab đánh giá đề thi
    @Query("""
                SELECT r FROM Review r
                JOIN FETCH r.account a
                JOIN FETCH r.test t
                JOIN FETCH t.course c
                WHERE r.course IS NULL
                  AND (:keyword IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:courseId IS NULL OR c.id = :courseId)
                  AND (:testId IS NULL OR t.id = :testId)
                  AND (:status IS NULL OR LOWER(r.status) = LOWER(:status))
                  AND (:rating IS NULL OR r.rating = :rating)
            """)
    Page<Review> findTestReviews(
            @Param("keyword") String keyword,
            @Param("courseId") Integer courseId,
            @Param("testId") Integer testId,
            @Param("status") String status,
            @Param("rating") Integer rating,
            Pageable pageable
    );


}
