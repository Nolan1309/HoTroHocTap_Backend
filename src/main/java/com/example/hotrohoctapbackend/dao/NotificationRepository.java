package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "notifications")
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    //    List<Notification> findByUserIdOrTopic(Long userId, String topic);
//    @Query("SELECT n FROM Notification n " +
//            "WHERE (n.userId = :userId AND n.isDeleted = false) " +
//            "OR (n.topic IN :topics AND n.userId IS NULL AND n.isDeleted = false) ORDER BY n.createdAt DESC ")
//    List<Notification> findNotificationsByUserOrTopic(
//            @Param("userId") Long userId,
//            @Param("topics") List<String> topics
//    );
    @Query(value = "SELECT noti.*, us.read_status as checked " +
            "FROM notifications noti " +
            "INNER JOIN user_notifications us " +
            "ON noti.id = us.notification_id " +
            "WHERE noti.is_deleted = 0 " +
            "AND us.account_id = :userId " +
            "ORDER BY noti.created_at DESC", nativeQuery = true)
    List<Object[]> findNotificationsByUserIdNative(@Param("userId") Long userId);

//    List<Notification> findByUserIdAndIsDeleted(Long userId, boolean isDeleted);
}
