package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminQuestionGetDTO;
import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/responsive-test/{testId}")
    public ResponseEntity<List<QuestionResponseDTO_User>> getQuestionsByTestId(@PathVariable Integer testId) {
        List<QuestionResponseDTO_User> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }

    // API để tải lên file Excel
    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestionsFromExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File không được để trống");
        }

        try {
            questionService.saveQuestionsFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Tải lên thành công");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xử lý file Excel");
        }
    }

    @DeleteMapping
    public String deleteQuestions(@RequestBody List<Integer> ids) {
        try {
            questionService.deleteQuestions(ids);
            return "Deleted successfully.";
        } catch (Exception e) {
            return "Error occurred while deleting questions.";
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportQuestionsToExcel() {
        byte[] excelData = questionService.exportQuestionsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    @GetMapping("/all")
    public Page<AdminQuestionGetDTO> getAllQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // Trả về các câu hỏi với phân trang
        return questionService.getAllQuestionsAdmin(page, size);
    }
    @GetMapping("/tests/questions/{testId}")
    public List<Question> getQuestionsByTestIdAdmin(@PathVariable Integer testId) {
        return questionService.getQuestionsByTestIdAdmin(testId);
    }
    @GetMapping("detail/{id}")
    public ResponseEntity<AdminQuestionGetDTO> getQuestionDetailsById(@PathVariable int id) {
        AdminQuestionGetDTO questionDTO = questionService.getQuestionDetailsByIdAdmin(id);
        if (questionDTO != null) {
            return ResponseEntity.ok(questionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody AdminQuestionGetDTO adminQuestionGetDTO) {
        try {
            questionService.addQuestionAdmin(adminQuestionGetDTO);
            return new ResponseEntity<>("Thêm câu hỏi thành công", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("update/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable int id, @RequestBody AdminQuestionGetDTO adminQuestionGetDTO) {
        try {
            boolean updated = questionService.updateQuestionAdmin(id, adminQuestionGetDTO);
            if (updated) {
                return new ResponseEntity<>("Cập nhật câu hỏi thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/hide/{id}")
    public ResponseEntity<?> deleteTestAdmin(@PathVariable int id) {
        try {
            Question deletedQuestion = questionService.deleteQuestionAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> activeQuestionAdmin(@PathVariable int id) {
        try {
            Question activedQuestion = questionService.activeQuestionAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
}
