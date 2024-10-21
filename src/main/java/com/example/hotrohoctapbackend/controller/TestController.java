package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.QuestionDTO_User;
import com.example.hotrohoctapbackend.DTO.TestDTO_User;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.service.QuestionService;
import com.example.hotrohoctapbackend.service.RedisTestService;
import com.example.hotrohoctapbackend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private RedisTestService redisTestService;
    @Autowired
    private TestService testService;
    @Autowired
    private QuestionService questionService;

    // Endpoint để lấy dữ liệu từ cache
    @GetMapping("/cache")
    public String getCache(@RequestParam String key) {
        return redisTestService.getFromCache(key);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDTO_User> getTestById(@PathVariable int id) {
        try {
            TestDTO_User testDTO = testService.getTestById(id);
            return ResponseEntity.ok(testDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{testId}/questions")
    public ResponseEntity<List<QuestionDTO_User>> getQuestionsByTestId(@PathVariable int testId) {
        List<QuestionDTO_User> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }
}
