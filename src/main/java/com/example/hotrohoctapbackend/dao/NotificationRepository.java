package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "notifications")
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    @Query(value = "SELECT id, is_deleted, message, title, topic FROM notifications", nativeQuery = true)
    Page<Object[]> findCustomNotificationsWithPagination(Pageable pageable);
}

