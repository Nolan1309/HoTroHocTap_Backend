package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_User;
import com.example.hotrohoctapbackend.convert.TestResultConverter;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.dao.TestResultRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.entity.TestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestResultService {
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CourseRepository courseRepository;

    public TestResultDTO_User addTestResult(TestResultDTO_User testResultDTOUser) {
        if (testResultDTOUser.getTestID() == null || testResultDTOUser.getAccountID() == null || testResultDTOUser.getCourseID() == null) {
            throw new IllegalArgumentException("Thiếu ID của Test, Account hoặc Course. Vui lòng kiểm tra lại.");
        }
        Optional<Test> optionalTest = testRepository.findById(testResultDTOUser.getTestID());
        Optional<Account> optionalAccount = accountRepository.findById(testResultDTOUser.getAccountID());
        Optional<Course> optionalCourse = courseRepository.findById(testResultDTOUser.getCourseID());

        if (!optionalTest.isPresent()) {
            throw new RuntimeException("Không tìm thấy Test với ID: " + testResultDTOUser.getTestID());
        }
        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("Không tìm thấy Account với ID: " + testResultDTOUser.getAccountID());
        }
        if (!optionalCourse.isPresent()) {
            throw new RuntimeException("Không tìm thấy Course với ID: " + testResultDTOUser.getCourseID());
        }
        TestResult testResult = new TestResult();
        testResult.setId(testResultDTOUser.getTestID());
        Test test = new Test();
        test.setId(testResultDTOUser.getTestID());
        testResult.setTest(test);

        Course course = new Course();
        course.setId(testResultDTOUser.getCourseID());
        testResult.setCourse(course);

        Account account = new Account();
        account.setId(testResultDTOUser.getAccountID());
        testResult.setAccount(account);

        testResult.setResult(testResultDTOUser.getResult());
        testResult.setScore(testResultDTOUser.getScore());
        testResult.setCorrect_answers(testResultDTOUser.getCorrect_answers());
        testResult.setIncorrect_answers(testResultDTOUser.getIncorrect_answers());
        testResult.setTotal_questions(testResultDTOUser.getTotal_questions());
        testResult.setCompletedAt(testResultDTOUser.getCompletedAt());

        TestResult testResult1 = testResultRepository.save(testResult);
        return TestResultConverter.convertToDTO(testResult1);

    }
}
