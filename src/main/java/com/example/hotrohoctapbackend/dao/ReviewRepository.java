package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.ReviewDTO;
import com.example.hotrohoctapbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    @Query(value = "SELECT review.* , ac.fullname, ac.image  from course_reviews review\n" +
            "inner join account ac on review.account_id = ac.id\n" +
            "where review.course_id = :courseId", nativeQuery = true)
    List<Object[]> findByCourseId(@Param("courseId") Integer courseId);

    Review findByCourseIdAndAccountId(@Param("courseId") Integer courseId , @Param("accountId") Integer accountId);
}
