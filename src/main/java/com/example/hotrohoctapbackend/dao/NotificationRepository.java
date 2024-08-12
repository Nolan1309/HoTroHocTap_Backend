package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "notifications")
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
}
