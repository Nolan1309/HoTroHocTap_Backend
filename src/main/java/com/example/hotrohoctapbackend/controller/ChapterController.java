package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.ChapterDTOAdmin;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.DTO.ChapterDTO;
import com.example.hotrohoctapbackend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/chapters")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }
    // Lấy danh sách các chương theo course_id
    @GetMapping("/course/{courseId}")
    public List<ChapterDTO> getChaptersByCourseId(@PathVariable int courseId) {
        return chapterService.findChaptersByCourseId(courseId);
    }
    @PostMapping("/add")
    public ResponseEntity<Chapter> addChapter(@RequestBody ChapterDTO chapterDTO) {
        // Gọi service để tạo chapter mới
        Chapter newChapter = chapterService.addChapter(chapterDTO);
        return ResponseEntity.ok(newChapter);
    }

    //ADmin get
    @GetMapping("/admin-all")
    public List<ChapterDTOAdmin> getChaptersAllAdmin() {
        return chapterService.findAllChapters();
    }



}
