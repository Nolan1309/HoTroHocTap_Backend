package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Subscription;
import com.example.hotrohoctapbackend.enums.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscription")
public interface SubcriptionRepository extends JpaRepository<Subscription, Integer> {
    @Query("SELECT s FROM Subscription s WHERE s.isDeleted = false AND" +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name IS NULL) " +
            "AND (s.status = :status OR :status IS NULL) " +
            "AND (s.duration_days = :durationDays OR :durationDays IS NULL)")
    Page<Subscription> findByFilters(@Param("name") String name,
                                     @Param("status") SubscriptionStatus status,
                                     @Param("durationDays") Integer durationDays,
                                     Pageable pageable);

}
