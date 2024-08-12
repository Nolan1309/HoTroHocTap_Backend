package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.User_Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user_notifications")
public interface User_NotificationRepository extends JpaRepository<User_Notification,Integer> {
}
