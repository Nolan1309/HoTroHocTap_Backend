package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.CourseBundle;
import com.example.hotrohoctapbackend.entity.Course_Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseBundleRepository extends JpaRepository<CourseBundle, Integer> {
    @Query("SELECT cb FROM CourseBundle cb WHERE cb.isDeleted = false AND " +
            "(LOWER(cb.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title IS NULL) " +
            "AND (cb.status = :status OR :status IS NULL)")
    Page<CourseBundle> findByTitleAndStatus(
            @Param("title") String title,
            @Param("status") Boolean status,
            Pageable pageable);

    @Query("SELECT cb FROM CourseBundle cb WHERE cb.isDeleted = false")
    List<CourseBundle> findByTitleAndStatusList();
}
