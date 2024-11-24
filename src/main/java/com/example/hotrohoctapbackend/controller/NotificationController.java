package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.AccountSendNotification_User;
import com.example.hotrohoctapbackend.DTO.User.NotificationRequestUser;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EnrolledCourseService enrolledCourseService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequestUser request) {
        Notification notification = notificationService.createNotification(
                request.getTitle(),
                request.getMessage(),
                request.getTopic()

        );
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserNotificationDTO_User>> getUserNotifications(
            @PathVariable Long userId) {
        List<UserNotificationDTO_User> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private User_NotificationRepository userNotificationRepository;

    //ADMIN SE XAI CAI NAY , TAO + THONG BAO NGAY
    @PostMapping("/notify")
    public ResponseEntity<?> notify(@RequestBody NotificationRequestUser request) {
        Notification notification = notificationService.createNotification(
                request.getTitle(), request.getMessage(), request.getTopic());

        if (request.getUserId() == null) {
            UserNotificationDTO_User user = new UserNotificationDTO_User(notification, false);
            List<AccountSendNotification_User> userIds = enrolledCourseService.getActiveEnrolledUsers();
            for (AccountSendNotification_User userId : userIds) {
                Account account = accountRepository.findById(userId.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
                User_Notification userNotification = new User_Notification();
                userNotification.setAccount(account);
                userNotification.setNotification(notification);
                userNotification.setRead_status(false);
                userNotificationRepository.save(userNotification);

//              emailService.sendNotificationEmail(userId.getEmail(), request.getTitle(), request.getMessage());
            }
            // Gửi toàn bộ User
            messagingTemplate.convertAndSend("/topic/" + request.getTopic(), user);
            return ResponseEntity.ok().build();
        } else {
            Optional<Account> account = accountRepository.findById(request.getUserId().intValue());

            UserNotificationDTO_User user = new UserNotificationDTO_User(notification, false);

            User_Notification userNotification = new User_Notification();
            userNotification.setAccount(account.get());
            userNotification.setNotification(notification);
            userNotification.setRead_status(false);
            userNotificationRepository.save(userNotification);

            //Gửi cho 1 user
            messagingTemplate.convertAndSendToUser(request.getUserId().toString(), "/queue/notifications", user);
//            emailService.sendNotificationEmail(account.get().getEmail(), request.getTitle(), request.getMessage());
            return ResponseEntity.ok().build();
        }
    }

    @PutMapping("/mark-all-as-read/{userId}")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        try {
            notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok("All notifications have been marked as read.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update notifications: " + e.getMessage());
        }
    }


    @PutMapping("/mark-as-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id, @RequestBody Integer notificationId) {
        notificationService.markAsRead(id, notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
}