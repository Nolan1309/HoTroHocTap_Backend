package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV2.QuestionCountDTO;
import com.example.hotrohoctapbackend.DTO.User.*;
import com.example.hotrohoctapbackend.entity.TestResult;
import com.example.hotrohoctapbackend.exception.ErrorResponse;
import com.example.hotrohoctapbackend.service.TestUserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-answers")
@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
public class TestUserAnswerController {

    @Autowired
    private TestUserAnswerService testUserAnswerService;

    @PostMapping("/add")
    public ResponseEntity<?> addTestUserAnswer(@RequestBody TestUserAnswerDTO_User testUserAnswerDTO) {
        try {
            TestUserAnswerDTO_User savedAnswer = testUserAnswerService.saveTestUserAnswer(testUserAnswerDTO);
            return new ResponseEntity<>(savedAnswer, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<ScoreResponseDTO_User> submitTestAnswers(@RequestBody TestUserAnswerRequestDTO_User requestDTO) {
        try {
            Map<String, Object> result = testUserAnswerService.saveTestUserAnswer(requestDTO);

            String status = (String) result.get("status");
            ScoreResponseDTO_User scoreResponse = (ScoreResponseDTO_User) result.get("scoreResponse");

//            Map<String, Object> response = new HashMap<>();
//            response.put("scoreResponse", scoreResponse);

            if ("unlocked".equals(status)) {
                return new ResponseEntity<>(scoreResponse, HttpStatus.OK); // 200 OK nếu đạt đủ điểm
            } else if ("locked".equals(status)) {
                return new ResponseEntity<>(scoreResponse, HttpStatus.CREATED); // 201 Created nếu không đủ điểm để mở khóa
            } else if ("course_completed".equals(status)) {
                return new ResponseEntity<>(scoreResponse, HttpStatus.ACCEPTED); //202 , Hoan thanh khoa hoc
            } else if ("already_completed".equals(status)) {
                return new ResponseEntity<>(scoreResponse, HttpStatus.ALREADY_REPORTED); //208 , Da ton tai qua trinh , ket qua luu , khong luu qua trinh
            } else {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error nếu có lỗi
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submit-progress-no-test")
    public ResponseEntity<?> submitProgressNoTest(@RequestBody UserAutoPassProgressDTO requestDTO) {
        try {
            testUserAnswerService.saveProgress(requestDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/exam/{testId}")
    public TestResultDTO_User postAnswerExamUserByTestId(@RequestBody QuestionUserExamPayload questionUserExamPayload) {

        TestResultDTO_User testResult = testUserAnswerService.saveTestUserAnswerExam(questionUserExamPayload);
//        return testUserAnswerService.getQuestionsCountByLevel(chapterID);
        return testResult;
    }
}