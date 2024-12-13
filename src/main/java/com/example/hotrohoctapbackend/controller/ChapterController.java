package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.ChapterDTOAdmin;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.DTO.ChapterDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @PutMapping("edit/{chapterId}")
    public ResponseEntity<Chapter> editChapter(
            @PathVariable Integer chapterId,            // Lấy chapterId từ URL
            @RequestBody ChapterDTO chapterDTO) {    // Lấy thông tin chapter mới từ request body
        try {
            // Gọi service để chỉnh sửa chapter
            Chapter updatedChapter = chapterService.editChapter(chapterId, chapterDTO);
            return ResponseEntity.ok(updatedChapter);  // Trả về chapter đã được cập nhật
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Trả về lỗi 404 nếu không tìm thấy chapter hoặc course
        }
    }
    //ADmin get
    @GetMapping("/admin-all")
    public List<ChapterDTOAdmin> getChaptersAllAdmin() {
        return chapterService.findAllChapters();
    }
    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideCommnetAdmin(@PathVariable int id) {
        try {
            Chapter hidedComment = chapterService.hideChapterAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }
    @PutMapping("/show/{id}")
    public ResponseEntity<?> showCommnetAdmin(@PathVariable int id) {
        try {
            Chapter showComment = chapterService.showChapterAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }


}
