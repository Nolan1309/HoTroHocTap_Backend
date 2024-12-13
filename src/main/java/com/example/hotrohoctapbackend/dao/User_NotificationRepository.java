package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.User_Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "user_notifications")
public interface User_NotificationRepository extends JpaRepository<User_Notification,Integer> {
    @Query("SELECT un FROM User_Notification un WHERE un.account.id = :userId")
    List<User_Notification> findByUserId(@Param("userId") Long userId);
    @Query("SELECT u FROM User_Notification u WHERE u.account.id = :accountId AND u.notification.id = :notificationId")
    Optional<User_Notification> findByAccountIdAndNotificationId(
            @Param("accountId") Integer accountId,
            @Param("notificationId") Integer notificationId);
}
