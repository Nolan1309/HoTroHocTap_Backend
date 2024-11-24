package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestResultDTO_View_User;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
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
        testResult.setChapterTested(testResultDTOUser.isChapterTest());
        testResult.setResult(testResultDTOUser.getResult());
        testResult.setScore(testResultDTOUser.getScore());
        testResult.setCorrect_answers(testResultDTOUser.getCorrect_answers());
        testResult.setIncorrect_answers(testResultDTOUser.getIncorrect_answers());
        testResult.setTotal_questions(testResultDTOUser.getTotal_questions());
        testResult.setCompletedAt(testResultDTOUser.getCompletedAt());

        TestResult testResult1 = testResultRepository.save(testResult);
        return TestResultConverter.convertToDTO(testResult1);

    }

    public Page<TestResultDTO_View_User> getTestResultsByAccountId(int page, int size, Integer accountId, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> results = testResultRepository.findTestResultsWithTestTitle(pageable, accountId, search);

        // Chuyển đổi Object[] thành DTO
        return results.map(result -> {
            TestResultDTO_View_User dto = new TestResultDTO_View_User();
            dto.setId((Integer) result[0]);
            dto.setCompletedAt(((Timestamp) result[1]).toLocalDateTime());
            dto.setCorrectAnswers((Integer) result[2]);
            dto.setIncorrectAnswers((Integer) result[3]);
            dto.setResult((String) result[4]);
            dto.setScore((Double) result[5]);
            dto.setTotalQuestions((Integer) result[6]);
            dto.setAccountId((Integer) result[7]);
            dto.setTestId((Integer) result[8]);
            dto.setCourseId((Integer) result[9]);
            dto.setDeletedDate(((Timestamp) result[10]).toLocalDateTime());
            dto.setDeleted((Boolean) result[11]);
            dto.setIsChapterTest((Boolean) result[12]);
            dto.setTestTitle((String) result[13]);
            return dto;
        });
    }

    public Double getAverageScoreUser(Long accountId, Long courseId) {
        return testResultRepository.calculateAverageScoreUser(accountId, courseId);
    }

    public Double getPassRateUser(Long accountId, Long courseId) {
        return testResultRepository.calculatePassRateUser(accountId, courseId);
    }

    public List<Object> getTestResultsUser(Long accountId, Long courseId) {
        return testResultRepository.getTestResultsByAccountAndCourseUser(accountId, courseId);
    }

    public List<Object[]> countResultsGroupedByResultUser(Long accountId, Long courseId) {
        return testResultRepository.countResultsGroupedByResultUser(accountId, courseId);
    }
}
