package com.example.hotrohoctapbackend.scheduler;

import com.example.hotrohoctapbackend.DTO.User.AccountSendNotification_User;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.SettingRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.SettingScheduler;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.entity.Notification;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import static com.example.hotrohoctapbackend.util.topic.NhacNhoHocBai;

@Component
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private User_NotificationRepository userNotificationRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private SettingRepository settingSchedulerRepository;

    private String getCronExpression() {
        List<SettingScheduler> schedulerList = settingSchedulerRepository.findByType("scheduler");
        for (SettingScheduler item : schedulerList)
        {
            if(item.isCheck()){
                return item.getName();
            }
        }
        return "0 0 7 * * ?";
    }
    private ScheduledFuture<?> scheduledTask;

    @PostConstruct
    public void scheduleDynamicCronJob() {
        if (scheduledTask != null) {
            scheduledTask.cancel(true); // Hủy lịch cũ nếu có
        }
        String cron = getCronExpression();
        System.out.println("Scheduling task with cron expression: " + cron);
        scheduledTask = taskScheduler.schedule(this::sendDailyNotifications, new CronTrigger(cron));
    }
//    @PostConstruct
//    public void scheduleDynamicCronJob() {
//        String cron = getCronExpression();
//        taskScheduler.schedule(this::sendDailyNotifications, new CronTrigger(cron));
//    }
    /**
     * Gửi thông báo tự động mỗi ngày vào lúc 8:00 sáng
     */
//    @Scheduled(cron = "0 0 7 * * ?")
    public void sendDailyNotifications() {
        List<AccountSendNotification_User> userIds = enrolledCourseService.getActiveEnrolledUsers();

        String title = "Nhắc nhở học bài";
        String message = "Bạn chưa hoàn thành khóa học ! Đừng quên tham gia nhé.";
        Notification notification = notificationService.createNotification(
                title,
                message,
                NhacNhoHocBai
        );
        UserNotificationDTO_User user = new UserNotificationDTO_User(notification, false);
        for (AccountSendNotification_User userId : userIds) {

            Account account = accountRepository.findById(userId.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            User_Notification userNotification = new User_Notification();
            userNotification.setAccount(account);
            userNotification.setNotification(notification);
            userNotification.setRead_status(false);
            userNotificationRepository.save(userNotification);

            try {
                emailService.sendNotificationEmail(userId.getEmail(), title, message);
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
            }

        }
        messagingTemplate.convertAndSend("/topic/" + NhacNhoHocBai, user);
    }
}
