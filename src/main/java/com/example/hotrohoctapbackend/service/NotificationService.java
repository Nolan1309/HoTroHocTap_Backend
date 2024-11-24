package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.NotificationRepository;
import com.example.hotrohoctapbackend.DTO.Admin.AdminNotificationDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

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
                        (String) record[4]   // topic
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
