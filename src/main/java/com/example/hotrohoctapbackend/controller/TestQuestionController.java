package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV2.AdminQuestionGetDTO_V2;
import com.example.hotrohoctapbackend.service.TestQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
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

    @PostMapping("/add-questions-v2/{testId}")
    public ResponseEntity<String> addQuestionsToTest_V2(
            @PathVariable Integer testId,
            @RequestBody List<AdminQuestionGetDTO_V2> questionDTOs
    ) {
        try {
            String result = testQuestionService.updateTestQuestions(testId, questionDTOs);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi không mong muốn: " + e.getMessage());
        }
    }

    @GetMapping("/questions/{testId}")
    public List<AdminQuestionGetDTO_V2> getQuestionsByTestId(@PathVariable Integer testId) {
        return testQuestionService.getQuestionsByTestId(testId);
    }

}
