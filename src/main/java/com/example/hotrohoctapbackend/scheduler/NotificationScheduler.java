package com.example.hotrohoctapbackend.scheduler;

import com.example.hotrohoctapbackend.DTO.AdminV2.PredictionRequestDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.StudentDataRequestDTO;
import com.example.hotrohoctapbackend.DTO.LearningPathSuggestionAPI;
import com.example.hotrohoctapbackend.DTO.User.AccountSendNotification_User;
import com.example.hotrohoctapbackend.DTO.User.StudentCourseProgressDTO;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.enums.ReminderType;
import com.example.hotrohoctapbackend.service.*;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.example.hotrohoctapbackend.service.services.PythonScriptService;
import com.example.hotrohoctapbackend.util.TOPIC;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;


@Component
public class NotificationScheduler {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EnrolledCourseService enrolledCourseService;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private ChapterRepository chapterRepository;
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
    @Autowired
    @Lazy
    private ProgressService progressService;
    @Autowired
    private StudentCourseDataService studentCourseDataService;
    @Autowired
    private PredictionResultRepository predictionResultRepository;
    @Autowired
    private PythonScriptService pythonScriptService;

    private String getCronExpression() {
        List<SettingScheduler> schedulerList = settingSchedulerRepository.findByReminderType(ReminderType.SCHEDULER);
        for (SettingScheduler item : schedulerList) {
            if (item.isCheck()) {
                return item.getName();
            }
        }
        return "0 0 7 * * ?";
    }

//    @Scheduled(cron = "0 0/30 * * * ?") // Lên lịch mỗi 30 phút
//    public void checkAndUpdateProgress() throws JsonProcessingException {
//
//        List<StudentCourseData> studentCourseDataList = studentCourseDataService.getAllStudent();
//        for (StudentCourseData studentCourseData : studentCourseDataList) {
//            Account account = accountRepository.findById(studentCourseData.getAccount().getId()).get();
//            if (account != null) {
//                List<Enrolled_Courses> enrolledCourses = enrolledCourseService.getAllEnroll_Course(account.getId());
//
//                for (Enrolled_Courses enrolled_courses : enrolledCourses) {
//                    Double percert = progressService.calculateProgress(account.getId(), enrolled_courses.getCourse().getId());
//                    if (percert >= 50) {
//                        studentCourseDataService.saveProgressDataHUIT(studentCourseData.getEmail(), studentCourseData.getStudentId(), account.getId(), enrolled_courses.getCourse().getId());
//                        PredictionRequestDTO predictionRequestDTO = new PredictionRequestDTO();
//                        predictionRequestDTO.setCourseId(enrolled_courses.getCourse().getId());
//                        List<StudentDataRequestDTO> studentDataRequestDTOList = new ArrayList<>();
//                        StudentDataRequestDTO item = new StudentDataRequestDTO();
//                        item.setAccountId(account.getId());
//                        item.setStudentId(studentCourseData.getStudentId());
//                        item.setEmail(studentCourseData.getEmail());
//                        studentDataRequestDTOList.add(item);
//                        predictionRequestDTO.setStudentsData(studentDataRequestDTOList);
//                        PredictionScheduler(predictionRequestDTO);
//                    }
//                }
//            }
//        }
//
//    }

    public void PredictionScheduler(PredictionRequestDTO predictionRequest) {
        // Lấy danh sách các đối tượng sinh viên và mã khóa học từ request
        List<StudentDataRequestDTO> studentsData = predictionRequest.getStudentsData();
        Integer courseId = predictionRequest.getCourseId();
        List<StudentCourseProgressDTO> studentCourseProgressDTOList = new ArrayList<>();
        for (StudentDataRequestDTO studentDataRequestDTO : studentsData) {
            StudentCourseProgressDTO item = studentCourseDataService.getCourseProgressFromStudent(studentDataRequestDTO.getStudentId(),
                    studentDataRequestDTO.getAccountId(), predictionRequest.getCourseId());

            studentCourseProgressDTOList.add(item);
        }

        List<LearningPathSuggestionAPI> studentPredictionDTOList = pythonScriptService.sendStudentData(studentCourseProgressDTOList);


        // Lưu kết quả dự đoán vào cơ sở dữ liệu (Dựa vào studentId để phân biệt sinh viên)
        for (LearningPathSuggestionAPI prediction : studentPredictionDTOList) {
            // Tìm accountId bằng cách so sánh studentId với studentsData
            String studentId = prediction.getStudent_id();
            Integer accountId = null;

            // Duyệt qua danh sách studentsData để tìm accountId
            for (StudentDataRequestDTO studentData : studentsData) {
                if (studentData.getStudentId().equals(studentId)) {
                    accountId = studentData.getAccountId();
                    break;  // Dừng vòng lặp khi tìm thấy match
                }
            }

            if (accountId != null) {
                // Tạo đối tượng PredictionResult để lưu vào cơ sở dữ liệu
                PredictionResult predictionResult = new PredictionResult();
                predictionResult.setStudentId(studentId);

                predictionResult.setCluster(prediction.getCluster().toString());
                predictionResult.setClusterDescription(prediction.getCluster_description());
                predictionResult.setClusterLabel(prediction.getCluster_label());
                predictionResult.setLearningPathSuggestion(prediction.getLearning_path_suggestion());
                predictionResult.setPrediction(prediction.getPrediction());
                predictionResult.setProbability(prediction.getProbability());
                predictionResult.setRiskLevel(prediction.getRisk_level());
                predictionResult.setCreatedAt(LocalDateTime.now());
                predictionResult.setAccount(accountRepository.findById(accountId).get());

                // Lưu đối tượng PredictionResult vào cơ sở dữ liệu
                predictionResultRepository.save(predictionResult);
            } else {
                // Nếu không tìm thấy accountId, có thể cần xử lý lỗi hoặc cảnh báo
                System.out.println("Không tìm thấy accountId cho studentId: " + studentId);
            }
        }
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

    public void scheduleDeleteExpiredDiscounts() {
        taskScheduler.schedule(this::deleteExpiredDiscounts, new CronTrigger("0 0 0 1 1/3 ?")); // Lịch trình 3 tháng một lần
    }

    public void scheduleDeleteExpiredAccounts() {
        taskScheduler.schedule(this::deleteExpiredAccounts, new CronTrigger("0 0 0 1 1/3 ?")); // Lịch trình 3 tháng một lần
    }

    public void scheduleDeleteExpiredCourses() {
        taskScheduler.schedule(this::deleteExpiredCourses, new CronTrigger("0 0 0 1 1/3 ?")); // Lịch trình 3 tháng một lần
    }

    public void scheduleDeleteExpiredChapters() {
        taskScheduler.schedule(this::deleteExpiredChapter, new CronTrigger("0 0 0 1 1/3 ?")); // Lịch trình 3 tháng một lần
    }

    public void scheduleDeleteExpiredLessons() {
        taskScheduler.schedule(this::deleteExpiredLessons, new CronTrigger("0 0 0 1 1/3 ?")); // Lịch trình 3 tháng một lần
    }


    @PostConstruct
    public void scheduleJobs() {
        scheduleDeleteExpiredDiscounts();
        scheduleDeleteExpiredAccounts();
        scheduleDeleteExpiredCourses();
        scheduleDeleteExpiredChapters();
        scheduleDeleteExpiredLessons();
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
                TOPIC.LEARNING
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
        messagingTemplate.convertAndSend("/topic/" + TOPIC.LEARNING, user);
    }


    public void deleteExpiredDiscounts() {
        List<Discount> deletedDiscounts = discountRepository.findByIsDeletedTrue();
        LocalDateTime now = LocalDateTime.now();

        for (Discount discount : deletedDiscounts) {
            if (discount.getDeletedDate().plusMonths(3).isBefore(now)) {
                discountRepository.delete(discount);

            }
        }
    }

    public void deleteExpiredAccounts() {
        // Lấy tất cả các tài khoản đã bị xóa
        List<Account> deletedAccounts = accountRepository.findByIsDeletedTrue();

        // Lấy ngày hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Duyệt qua từng tài khoản đã bị xóa
        for (Account account : deletedAccounts) {
            // Nếu tài khoản đã bị xóa hơn 3 tháng, xóa nó khỏi hệ thống
            if (account.getDeletedDate().plusMonths(3).isBefore(now)) {
                accountRepository.delete(account);
                System.out.println("Đã xóa tài khoản " + account.getId() + " khỏi hệ thống.");
            }
        }
    }

    public void deleteExpiredCourses() {
        // Lấy tất cả các khóa học đã bị xóa
        List<Course> deletedCourses = courseRepository.findByIsDeletedTrue();

        // Lấy ngày hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Duyệt qua từng khóa học đã bị xóa
        for (Course course : deletedCourses) {
            // Nếu khóa học đã bị xóa hơn 3 tháng, xóa nó khỏi hệ thống
            if (course.getDeletedDate().plusMonths(3).isBefore(now)) {
                courseRepository.delete(course);
                System.out.println("Đã xóa khóa học " + course.getId() + " khỏi hệ thống.");
            }
        }
    }

    public void deleteExpiredLessons() {
        // Lấy tất cả các khóa học đã bị xóa
        List<Lesson> deletedCourses = lessonRepository.findByIsDeletedTrue();

        // Lấy ngày hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Duyệt qua từng khóa học đã bị xóa
        for (Lesson course : deletedCourses) {
            // Nếu khóa học đã bị xóa hơn 3 tháng, xóa nó khỏi hệ thống
            if (course.getDeletedDate().plusMonths(3).isBefore(now)) {
                lessonRepository.delete(course);
                System.out.println("Đã xóa khóa học " + course.getId() + " khỏi hệ thống.");
            }
        }
    }

    public void deleteExpiredChapter() {
        // Lấy tất cả các khóa học đã bị xóa
        List<Chapter> deletedCourses = chapterRepository.findByIsDeletedTrue();

        // Lấy ngày hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Duyệt qua từng khóa học đã bị xóa
        for (Chapter course : deletedCourses) {
            // Nếu khóa học đã bị xóa hơn 3 tháng, xóa nó khỏi hệ thống
            if (course.getDeletedDate().plusMonths(3).isBefore(now)) {
                chapterRepository.delete(course);
                System.out.println("Đã xóa khóa học " + course.getId() + " khỏi hệ thống.");
            }
        }
    }


}
