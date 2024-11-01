package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.User.ScoreResponseDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestUserAnswerDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestUserAnswerRequestDTO_User;
import com.example.hotrohoctapbackend.exception.ErrorResponse;
import com.example.hotrohoctapbackend.service.TestUserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-answers")
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
            ScoreResponseDTO_User scoreResponse = testUserAnswerService.saveTestUserAnswer(requestDTO);
            return new ResponseEntity<>(scoreResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}