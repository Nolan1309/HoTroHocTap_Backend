package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.service.TestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/test-questions")
public class TestQuestionController {
    @Autowired
    private TestQuestionService testQuestionService;
    @PostMapping("/add-questions/{testId}")
    public ResponseEntity<String> addQuestionsToTest(
            @PathVariable Integer testId,
            @RequestBody List<Integer> questionIds) {
        try {
            String result = testQuestionService.addQuestionsToTest(testId, questionIds);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi không mong muốn: " + e.getMessage());
        }
    }

}
