package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Discount;
import com.example.hotrohoctapbackend.enums.DiscountFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "discounts")
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    @Query(value = "SELECT d.id, d.description, d.discount_value, d.title, d.is_deleted " +
            "FROM discounts d",
            countQuery = "SELECT COUNT(*) FROM discounts d",
            nativeQuery = true)
    Page<Object[]> findDiscounts(Pageable pageable);

    @Query(value = "SELECT d.id, d.description, d.discount_value, d.title, d.end_date, d.start_date " +
            "FROM discounts d WHERE d.id = :id",
            nativeQuery = true)
    List<Object[]> findDiscountById(@Param("id") Integer id);

    List<Discount> findByIsDeletedTrue();

    Discount findTopByOrderByCreatedAtDesc();

    @Query("SELECT d FROM Discount d WHERE d.isDeleted = false AND (:title IS NULL OR d.title LIKE %:title%) " +
            "AND (:format IS NULL OR d.format = :format)")
    Page<Discount> findByTitleAndDiscountType(
            @Param("title") String title,
            @Param("format") DiscountFormat format,
            Pageable pageable);
}
