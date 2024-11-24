package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminNotificationDTO;
import com.example.hotrohoctapbackend.entity.Discount;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/getall")
    public Page<AdminNotificationDTO> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationService.getNotifications(pageable);
    }
    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideNotificationAdmin(@PathVariable int id) {
        try {
            Notification hidedNotification = notificationService.hideNotificationAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
    @PutMapping("/show/{id}")
    public ResponseEntity<?> showNotificationAdmin(@PathVariable int id) {
        try {
            Notification showNotification = notificationService.showNotificationAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
}
