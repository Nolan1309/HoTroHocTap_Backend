package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.service.QuestionService;
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
    private QuestionService questionService ;

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
}
