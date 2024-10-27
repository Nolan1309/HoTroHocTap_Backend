package com.example.hotrohoctapbackend.convert;

import com.example.hotrohoctapbackend.DTO.TestResultDTO_User;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.entity.TestResult;

public class TestResultConverter {

    // Chuyển đổi từ Entity sang DTO
    public static TestResultDTO_User convertToDTO(TestResult testResult) {
        return new TestResultDTO_User(
                testResult.getId(),
                testResult.getTest().getId(),
                testResult.getAccount().getId(),
                testResult.getCourse().getId(),
                testResult.getScore(),
                testResult.getCorrect_answers(),
                testResult.getIncorrect_answers(),
                testResult.getTotal_questions(),
                testResult.getCompletedAt(),
                testResult.getResult()
        );
    }

    // Chuyển đổi từ DTO sang Entity
    public static TestResult convertToEntity(TestResultDTO_User dto) {
        TestResult testResult = new TestResult();
        testResult.setId(dto.getId());

        // Tạo các đối tượng Test, Account và Course chỉ với ID
        Test test = new Test();
        test.setId(dto.getTestID());
        testResult.setTest(test);

        Account account = new Account();
        account.setId(dto.getAccountID());
        testResult.setAccount(account);

        Course course = new Course();
        course.setId(dto.getCourseID());
        testResult.setCourse(course);

        // Thiết lập các thuộc tính khác
        testResult.setScore(dto.getScore());
        testResult.setCorrect_answers(dto.getCorrect_answers());
        testResult.setIncorrect_answers(dto.getIncorrect_answers());
        testResult.setTotal_questions(dto.getTotal_questions());
        testResult.setCompletedAt(dto.getCompletedAt());
        testResult.setResult(dto.getResult());

        return testResult;
    }
}