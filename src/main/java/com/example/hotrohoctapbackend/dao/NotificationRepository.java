package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.util.TOPIC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "notifications")
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Notification findByTopic(TOPIC topic);

    @Query(value = "SELECT noti.id, noti.created_at, noti.title, noti.updated_at, noti.deleted_date, noti.is_deleted, noti.topic, noti.message, us.read_status as checked " +
            "FROM notifications noti " +
            "INNER JOIN user_notifications us " +
            "ON noti.id = us.notification_id " +
            "WHERE noti.is_deleted = 0 " +
            "AND us.account_id = :userId " +
            "ORDER BY noti.created_at DESC", nativeQuery = true)
    List<Object[]> findNotificationsByUserIdNative(@Param("userId") Long userId);


    @Query(value = "SELECT noti.id, noti.created_at, noti.title, noti.updated_at, noti.deleted_date, noti.is_deleted, noti.topic, noti.message, us.read_status as checked " +
            "FROM notifications noti " +
            "INNER JOIN user_notifications us " +
            "ON noti.id = us.notification_id " +
            "WHERE noti.is_deleted = 0 " +
            "AND us.account_id = :userId AND noti.id = :notificationId " +
            "ORDER BY noti.created_at DESC", nativeQuery = true)
    List<Object[]> findNotificationsByUserIdNativeAndNotificationId(@Param("userId") Long userId, @Param("notificationId") Long notificationId);

    @Query(value = "SELECT id, is_deleted, message,title ,topic,created_at, deleted_date, updated_at FROM notifications", nativeQuery = true)
    Page<Object[]> findCustomNotificationsWithPagination(Pageable pageable);
//    List<Notification> findByUserIdAndIsDeleted(Long userId, boolean isDeleted);
}
