package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.ProgressDTO_User;
import com.example.hotrohoctapbackend.entity.Progress;
import com.example.hotrohoctapbackend.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/progress")
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    @GetMapping("/{courseId}/progress/{accountId}")
    public ResponseEntity<List<ProgressDTO_User>> getProgress(@PathVariable Integer courseId, @PathVariable Integer accountId) {
        List<ProgressDTO_User> progressList = progressService.getProgressByCourseAndAccount(courseId, accountId);
        return ResponseEntity.ok(progressList);
    }
    @PostMapping("/add")
    public ResponseEntity<Progress> addProgress(@RequestBody ProgressDTO_User progressDTO) {
        Progress progress = progressService.addOrUpdateProgress(progressDTO);
        return ResponseEntity.ok(progress);
    }
}
