package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Course_Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "course_discounts")
public interface Course_DiscountRepository extends JpaRepository<Course_Discount,Integer> {
    @Query(value = "SELECT cd.id, cd.deleted_date, cd.is_deleted, cd.course_id, cd.discount_id, cd.is_check " +
            "FROM course_discounts cd WHERE cd.course_id = :courseId", nativeQuery = true)
    List<Object[]> findCourseDiscountsByCourseId(@Param("courseId") Integer courseId);
    @Modifying
    @Transactional
    @Query(value = "UPDATE course_discounts cd SET cd.is_check = false WHERE cd.id = :courseDiscountId", nativeQuery = true)
    void updateIsCheckToFalse(@Param("courseDiscountId") Integer courseDiscountId);
    @Query(value = "SELECT COUNT(*) > 0 FROM course_discounts WHERE course_id = :courseId AND discount_id = :discountId AND is_deleted = 0", nativeQuery = true)
    Integer existsByCourseAndDiscountAndNotDeleted(@Param("courseId") int courseId, @Param("discountId") int discountId);
    @Query("SELECT cd FROM Course_Discount cd WHERE cd.course.id = :courseId AND cd.discount.id = :discountId AND cd.isDeleted = false")
    Optional<Course_Discount> findByCourseIdAndDiscountId(@Param("courseId") int courseId, @Param("discountId") int discountId);
}
