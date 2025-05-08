package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminNotificationDTO;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.NotificationRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.util.TOPIC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;
    @Autowired
    private User_NotificationRepository userNotificationRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createNotification(String title, String message, TOPIC topic) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setTopic(topic.name());
//        notification.setUserId(userId);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        return repository.save(notification);
    }

    public Notification getNotificationByTopic(TOPIC topic) {
        String topicValue = topic.name();
        return repository.findByTopic(topicValue);
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

    public List<UserNotificationDTO_User> getUserNotificationsDetail(Long userId, Long notificationId) {

        User_Notification userNotification = userNotificationRepository.findByAccountIdAndNotificationId(userId.intValue(), notificationId.intValue())
                .orElseThrow(() -> new RuntimeException("Notification not found for this user"));
        userNotification.setRead_status(true);
        userNotificationRepository.save(userNotification);

        List<Object[]> results = repository.findNotificationsByUserIdNativeAndNotificationId(userId, notificationId);

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

    public void markAsReadDetail(Integer accountId, Integer notificationId) {
        User_Notification notification = userNotificationRepository.findByAccountIdAndNotificationId(accountId, notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found for this user"));
        notification.setRead_status(true);
        userNotificationRepository.save(notification);
    }

    public void sendNotificationToUser(Long userId, String message) {
        String destination = "/user/" + userId + "/notifications";
        messagingTemplate.convertAndSend(destination, message);
    }

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<AdminNotificationDTO> getNotifications(Pageable pageable) {
        Page<Object[]> notifications = notificationRepository.findCustomNotificationsWithPagination(pageable);

        // Ánh xạ từ Object[] sang AdminNotificationDTO
        List<AdminNotificationDTO> notificationDTOs = notifications.getContent().stream().map(record ->
                new AdminNotificationDTO(
                        (Integer) record[0], // id
                        (Boolean) record[1], // isDeleted
                        (String) record[2],  // message
                        (String) record[3],  // title
                        (String) record[4],   // topic
                        ((java.sql.Timestamp) record[5]).toLocalDateTime(),
                        ((java.sql.Timestamp) record[6]).toLocalDateTime(),
                        ((java.sql.Timestamp) record[7]).toLocalDateTime()
                )
        ).collect(Collectors.toList());

        // Trả về Page với danh sách DTO
        return new PageImpl<>(notificationDTOs, pageable, notifications.getTotalElements());
    }

    public Notification hideNotificationAdmin(int notificationID) {
        // Tìm tài khoản theo ID
        Optional<Notification> accountOpt = notificationRepository.findById(notificationID);

        if (accountOpt.isPresent()) {
            Notification account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return notificationRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + notificationID);
        }
    }

    public Notification showNotificationAdmin(int notificationID) {
        // Tìm tài khoản theo ID
        Optional<Notification> accountOpt = notificationRepository.findById(notificationID);

        if (accountOpt.isPresent()) {
            Notification account = accountOpt.get();
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return notificationRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + notificationID);
        }
    }
}
