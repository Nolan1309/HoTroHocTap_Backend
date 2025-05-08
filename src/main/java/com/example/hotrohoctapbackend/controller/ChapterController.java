package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.ChapterDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminChapterDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminCourseDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Chapter.ChapterDTOAdminV3;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.DTO.ChapterDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.ChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
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

    @GetMapping("/courses/{courseId}")
    public ApiResponse<List<ChapterDTOAdminV3>> getChapters(@PathVariable int courseId) {
        List<ChapterDTOAdminV3> chapters = chapterService.getChaptersByCourseId(courseId);
        return new ApiResponse<>(200, "Success", chapters);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Boolean>> addChapter(@RequestBody ChapterDTO chapterDTO) {

        try {
            Boolean newChapter = chapterService.addChapter(chapterDTO);

            // Trả về phản hồi thành công với dữ liệu bài học đã mở khóa
            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Thêm chương thành công!",
                    newChapter
            ));

        } catch (ResponseStatusException e) {
            // Nếu lỗi được ném ra từ Service, trả về JSON theo ApiResponse
            return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse<>(
                    e.getStatusCode().value(),
                    e.getReason(),
                    null
            ));
        } catch (Exception e) {
            // Nếu lỗi khác xảy ra, trả về lỗi 500 (Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Lỗi Server!",
                    null
            ));
        }

    }

    @PutMapping("/edit/{chapterId}")
    public ResponseEntity<Chapter> editChapter(
            @PathVariable Integer chapterId,
            @RequestBody ChapterDTO chapterDTO) {
        try {
            Chapter updatedChapter = chapterService.editChapter(chapterId, chapterDTO);
            return ResponseEntity.ok(updatedChapter);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
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

    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> UnlockChapterAdmin(@PathVariable int id) {
        try {
            Chapter unlockChapter = chapterService.unlockChapterAdmin(id);
            return ResponseEntity.ok().body("Chapter with ID " + id + " marked as unlock.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chapter not found with ID: " + id);
        }
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> LockChapterAdmin(@PathVariable int id) {
        try {
            Chapter lockChapter = chapterService.lockChapterAdmin(id);
            return ResponseEntity.ok().body("Chapter with ID " + id + " marked as lock.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chapter not found with ID: " + id);
        }
    }

    @GetMapping("/restore/list-all-chapters")
    public Page<AdminChapterDTORestoreList> getDeletedCourses(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (courseId != null && !courseId.equals("NaN")) {
            return chapterService.getDeletedChapterByCourseId(courseId, pageRequest);
        } else {
            return chapterService.getDeletedChapter(pageRequest);
        }
    }

    @GetMapping("/restore-no-delete/list-all-no-chapters")
    public List<AdminChapterDTORestoreList> getNoDeletedCourses(@RequestParam(required = false) Integer courseId) {
        return chapterService.getNoDeletedChapter(courseId);
    }

    @GetMapping("/restore/list-all/search-chapters")
    public ResponseEntity<Page<AdminChapterDTORestoreList>> searchAccounts(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String chapterTitle,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (courseId != null) {
            Page<AdminChapterDTORestoreList> result = chapterService.getDeletedChapterSearchByCourseId(courseId, chapterTitle, deletedDate, page, size);
            return ResponseEntity.ok(result);
        } else {
            Page<AdminChapterDTORestoreList> result = chapterService.getDeletedChapterSearch(chapterTitle, deletedDate, page, size);
            return ResponseEntity.ok(result);
        }
    }

    @PutMapping("/restore/{chapterId}")
    public ResponseEntity<Chapter> restoreChapter(@PathVariable Integer chapterId) {
        AdminChapterDTORestoreList chapterDTORestoreList = new AdminChapterDTORestoreList();
        chapterDTORestoreList.setId(chapterId);
        Chapter restoreChapter = chapterService.updateRestoreChapter(chapterDTORestoreList);
        return ResponseEntity.ok(restoreChapter);
    }

    @DeleteMapping("/delete/{chapterId}")
    public ResponseEntity<String> deleteChapter(@PathVariable Integer chapterId) {
        AdminChapterDTORestoreList chapterDTORestoreList = new AdminChapterDTORestoreList();
        chapterDTORestoreList.setId(chapterId);
        chapterService.deleteRestoreChapter(chapterDTORestoreList);
        return ResponseEntity.ok("Chapter permanently deleted.");
    }

}
