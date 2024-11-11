package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    // API thêm lesson mới
    @PostMapping("/add")
    public ResponseEntity<Lesson> addLesson(@RequestBody LessonDTO2 lessonDTO2) {
        Lesson lesson = lessonService.addLesson(lessonDTO2);
        return ResponseEntity.ok(lesson);
    }
}
