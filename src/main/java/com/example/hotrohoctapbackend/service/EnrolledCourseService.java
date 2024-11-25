package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.CountCourseDTO;
import com.example.hotrohoctapbackend.DTO.User.AccountSendNotification_User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.Enrolled_CoursesRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Enrolled_Courses;
import com.example.hotrohoctapbackend.entity.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrolledCourseService {
    @Autowired
    private Enrolled_CoursesRepository enrolledCoursesRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String enrollInCourse(Integer accountId, Integer courseId) {

        if (isUserEnrolled(accountId.longValue(), courseId.longValue())) {
            return "Actived Faild";
        }

        // Nếu chưa đăng ký, tiến hành đăng ký
        Enrolled_Courses enrolledCourses = new Enrolled_Courses();
        Account account = new Account();
        account.setId(accountId);

        RoleUser role = new RoleUser();
        role.setId(2);
        role.setRoleName("USER");
        account.setRole(role);

        enrolledCourses.setAccount(account);

        Course course = new Course();
        course.setId(courseId);
        enrolledCourses.setCourse(course);
        enrolledCourses.setEnrollmentDate(LocalDateTime.now());
        enrolledCourses.setStatus("Actived");

        enrolledCoursesRepository.save(enrolledCourses);
        return "Actived Success";
    }

    public boolean isUserEnrolled(Long userId, Long courseId) {
        return enrolledCoursesRepository.findEnrolledCourse(userId, courseId).isPresent();
    }

    public CountCourseDTO getEnrolledCoursesByAccountId(Integer accountId) {
        List<Enrolled_Courses> enrolledCourses = enrolledCoursesRepository.findByAccountId(accountId);

        CountCourseDTO item = new CountCourseDTO();
        item.setTotalCourse(enrolledCourses.size());
        Integer totalCountCompleted = 0;
        Integer totalCountStudying = 0;
        for (Enrolled_Courses courses : enrolledCourses) {
            if (courses.getStatus().equals("Completed")) {  // Use .equals() to compare string values
                totalCountCompleted++;
            }
            if (courses.getStatus().equals("Studying")) {  // Use .equals() to compare string values
                totalCountStudying++;
            }

        }
        item.setTotalCourseStudying(totalCountStudying);
        item.setTotalCourseComplete(totalCountCompleted);

        return item;  // returns the number of enrolled courses
    }

//    public Page<CourseDTO_User_Profile> getCoursesByAccountId(Integer accountId, int page, int size) {
//        Page<Object[]> results = enrolledCoursesRepository.findCoursesByAccountIdNative(accountId, PageRequest.of(page, size));
//
//        return results.map(result -> new CourseDTO_User_Profile(
//                (Integer) result[0],     // id
//                (String) result[1],      // duration
//                (String) result[2],      // image_url
//                (String) result[3],      // courses_title
//                (DateTime) result[4]  // enrollment_date
//        ));
//    }

    public List<AccountSendNotification_User> getActiveEnrolledUsers() {
        List<Object[]> results = enrolledCoursesRepository.findActiveEnrolledUsers();
        return results.stream()
                .map(r -> new AccountSendNotification_User((Integer) r[0], (String) r[1], (String) r[2]))
                .collect(Collectors.toList());
    }

    public List<AccountSendNotification_User> getAllAccount() {
        List<Account> results = accountRepository.findAll();
        List<AccountSendNotification_User> allUser = new ArrayList<>();

        for (Account account : results) {
            AccountSendNotification_User accountSendNotificationUser = new AccountSendNotification_User(account.getId(), account.getUsername(), account.getEmail());

            allUser.add(accountSendNotificationUser);
        }
        return allUser;
    }

}
