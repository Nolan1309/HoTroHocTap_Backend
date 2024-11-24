package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.NotificationRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private User_NotificationRepository userNotificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(String title, String message, String topic) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTopic(topic);
//        notification.setUserId(userId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    public List<UserNotificationDTO_User> getUserNotifications(Long userId) {
        List<Object[]> results = repository.findNotificationsByUserIdNative(userId);

        List<UserNotificationDTO_User> notifications = new ArrayList<>();

        for (Object[] row : results) {
            Notification notification = new Notification();
            notification.setId((Integer) row[0]);
            notification.setCreatedAt(((Timestamp) row[1]).toLocalDateTime());
            notification.setTitle((String) row[2]);
            notification.setUpdatedAt(((Timestamp) row[3]).toLocalDateTime());
            notification.setDeletedDate(((Timestamp) row[4]).toLocalDateTime());
            notification.setDeleted((Boolean) row[5]);
            notification.setTopic((String) row[6]);
            notification.setMessage((String) row[7]);
            boolean readStatus = (Boolean) row[8];

            notifications.add(new UserNotificationDTO_User(notification, readStatus));
        }

        return notifications;
    }

    public void getNotificationsByUserId(Long userId) {
        List<User_Notification> userNotificationList = userNotificationRepository.findByUserId(userId);
        userNotificationList.forEach(notification -> notification.setRead_status(true));
        userNotificationRepository.saveAll(userNotificationList);
    }

    public void markAsRead(Integer accountId, Integer notificationId) {
        User_Notification notification = userNotificationRepository.findByAccountIdAndNotificationId(accountId, notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found for this user"));
        notification.setRead_status(true);
        userNotificationRepository.save(notification);
    }

    public void sendNotificationToUser(Long userId, String message) {
        String destination = "/user/" + userId + "/notifications";
        messagingTemplate.convertAndSend(destination, message);
    }

//    public void updateAllNotificationsChecked(Long userId) {
//        List<Notification> notifications = repository.findByUserIdAndIsDeleted(userId, false);
//        notifications.forEach(notification -> notification.setChecked(true));
//        repository.saveAll(notifications);
//    }
}
