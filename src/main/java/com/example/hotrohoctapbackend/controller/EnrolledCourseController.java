package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminCourseEnrolledDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Student.StudentDTO;
import com.example.hotrohoctapbackend.DTO.CountCourseDTO;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.EnrolledCourseService;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.example.hotrohoctapbackend.util.TOPIC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/enrolled-course")
public class EnrolledCourseController {

    @Autowired
    private EnrolledCourseService enrolledCourseService;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private User_NotificationRepository userNotificationRepository;

    @GetMapping("/check-enrollment")
    public String checkUserEnrollment(@RequestParam Long userId, @RequestParam Long courseId) {
        boolean isEnrolled = enrolledCourseService.isUserEnrolled(userId, courseId);
        if (isEnrolled) {
            return "Actived";
        } else {
            return "NoActived";
        }
    }

    @GetMapping("/courses/{courseId}/students")
    public ApiResponse<List<StudentDTO>> getStudentsByCourse(
            @PathVariable int courseId,
            @RequestParam(required = false, defaultValue = "") String searchTerm, // Tìm kiếm theo tên học viên
            @RequestParam(required = false, defaultValue = "USER,USERVIP") String roles, // Vai trò (USER, USERVIP)
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "10") int size // Số lượng bản ghi mỗi trang mặc định là 10
    ) {
        // Chuyển đổi chuỗi roles thành List
        List<String> roleList = Arrays.asList(roles.split(","));
        return enrolledCourseService.getStudentsByCourseId(courseId, searchTerm, roleList, page, size);
    }

    @PostMapping("/enrollment-complete")
    public String checkUserEnrollmentComplete(@RequestParam Long userId, @RequestParam Long courseId) {
        boolean isEnrolled = enrolledCourseService.isUserEnrolled(userId, courseId);
        if (isEnrolled) {
            return "OK";
        } else {
            return "NO";
        }
    }


    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCourse(@RequestParam Integer accountId, @RequestParam Integer courseId) {
        try {
            String result = enrolledCourseService.enrollInCourse(accountId, courseId);

            if ("Actived Faild".equals(result)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

            Optional<Account> account = accountRepository.findById(accountId);
            String title = "Đăng ký khóa học";
            String getMessage = "Tài khoản " + account.get().getFullname() + " đăng ký khóa học thành công!";

            Notification notification = notificationService.createNotification(
                    title, getMessage, TOPIC.ENROLL_COURSE);

            Account account2 = accountRepository.findById(account.get().getId()).orElseThrow(() -> new RuntimeException("User not found"));
            User_Notification userNotification = new User_Notification();
            userNotification.setAccount(account2);
            userNotification.setNotification(notification);
            userNotification.setRead_status(false);
            userNotificationRepository.save(userNotification);

            UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);
            messagingTemplate.convertAndSendToUser(String.valueOf(account.get().getId()), "/queue/notifications", notificationDTOUser);
            try {
                emailService.sendNotificationEmail(account.orElseThrow().getEmail(), title, getMessage);
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/count/{accountId}")
    public ResponseEntity<CountCourseDTO> enrollCourseCount(@PathVariable Integer accountId) {
        try {
            CountCourseDTO result = enrolledCourseService.getEnrolledCoursesByAccountId(accountId);

            if (result.getTotalCourse() <= 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);  // If no courses, return 204 No Content
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/accounts/courses/{courseId}")
    public Page<AdminCourseEnrolledDTO> getAccountsByCourse(@PathVariable int courseId,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return enrolledCourseService.getAccountsByCourseId(courseId, pageable);
    }

    @GetMapping("/status")
    public String getStatus(@RequestParam Integer accountId, @RequestParam Integer courseId) {
        return enrolledCourseService.getStatusByAccountAndCourse(accountId, courseId);
    }
//    @PostMapping("/update-status")
//    public ResponseEntity<String> updateStatus(
//            @RequestParam Integer accountId,
//            @RequestParam Integer courseId) {
//        try {
//            String message = enrolledCourseService.updateStatus(accountId, courseId);
//            return ResponseEntity.ok(message);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }


    @GetMapping("/{accountId}/enrolled")
    public ResponseEntity<List<String>> getEnrolledAccounts(@PathVariable Long accountId) {
        List<String> enrolledAccounts = enrolledCourseService.getEnrolledAccounts(accountId);
        return ResponseEntity.ok(enrolledAccounts); // Trả về danh sách account_id dưới dạng List<String>
    }

    @GetMapping("/api/course-authors/{accountId}")
    public List<String> getCourseAuthors(@PathVariable Long accountId) {
        return enrolledCourseService.getCourseAuthors(accountId);
    }

}
