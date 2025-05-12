package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminLessonGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.LessonDTOVideo_Admin;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminChapterDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLessonsDTOList;
import com.example.hotrohoctapbackend.DTO.AdminV2.AdminLesssonDTORestoreList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Lesson.LessonDTO;
import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.LessonService;
import com.example.hotrohoctapbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    // API thêm lesson mới
    @PostMapping("/add")
    public ResponseEntity<Lesson> addLesson(@RequestBody LessonDTO2 lessonDTO2) {
        Lesson lesson = lessonService.addLesson(lessonDTO2);
        return ResponseEntity.ok(lesson);
    }

    @PutMapping("/update/{lessonId}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Integer lessonId, @RequestBody LessonDTO2 lessonDTO2) {
        Lesson lesson = lessonService.updateLesson(lessonId, lessonDTO2);
        return ResponseEntity.ok(lesson);
    }

    //Danh sách bài học của khóa học
    @GetMapping("/course/{courseId}")
    public ApiResponse<List<LessonDTO>> getLessonsByCourseId(@PathVariable int courseId) {
        List<LessonDTO> lessons = lessonService.getLessonsByCourseId(courseId);
        return new ApiResponse<>(200, "Lessons fetched successfully", lessons);
    }


    @PostMapping("/add-lesson-with-video")
    public ResponseEntity<?> addLessonWithVideo(@RequestParam("files") MultipartFile[] files,
                                                @RequestParam("chapterId") Integer chapterId,
                                                @RequestParam("courseId") Integer courseId) throws IOException {
        if (files.length == 0) {
            return ResponseEntity.badRequest().body("Không có tệp nào được chọn.");
        }
        if (chapterId == null) {
            return ResponseEntity.badRequest().body("Không có chapterId.");
        }
        videoService.uploadVideoAllItem(files, courseId, chapterId);
        return ResponseEntity.ok("Tải lên thành công");
    }

    @GetMapping("/{id}")
    public List<LessonDTOVideo_Admin> getLessonByIdAdmin(@PathVariable int id) {
        return lessonService.getLessonVideoTestDataByLessonId(id);
    }

    @GetMapping("/detail/{id}")
    public AdminLessonsDTOList getLessonDetailById(@PathVariable int id) {
        return lessonService.getLessonDetailById(id);
    }

    @PutMapping("/update-lesson-video")
    public ResponseEntity<?> updateLessonWithVideoInformation(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("topic") String topic,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoId") int videoId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam("documentShort") String documentShort,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "documentUrlFile", required = false) MultipartFile documentUrlFile,
            @RequestPart(value = "videoUrl", required = false) String videoUrl,
            @RequestPart(value = "documentUrl", required = false) String documentUrl
    ) {
        try {
            // Tìm Lesson và cập nhật thông tin
            Lesson lessonOptional = lessonRepository.findById(id).get();
            if (lessonOptional == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson không tồn tại");
            }
            lessonOptional.setTitle(title);
            lessonOptional.setTopic(topic);
            Optional<Course> course = courseRepository.findById(courseId);
            lessonOptional.setCourse(course.get());
            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            lessonOptional.setChapter(chapter.get());
            lessonOptional.setUpdatedAt(LocalDateTime.now());
            lessonService.updateLessonAdmin(lessonOptional);


            Video video = videoRepository.findById(videoId).get();

            video.setLesson(lessonOptional);
            video.setTitle(videoTitle);
            video.setDocumentShort(documentShort);
            video.setCreatedAt(LocalDateTime.now());
            video.setUpdatedAt(LocalDateTime.now());
            if (documentShort != null) {
                video.setDocumentShort(documentShort);
            }
            if (documentUrl != null) {
                video.setDocumentUrl(documentUrl);
            }

            if (videoUrl != null) {
                video.setUrl(videoUrl);
            }

            if (videoFile != null) {
                String videoURL = videoService.uploadVideo(videoFile);
                video.setUrl(videoURL);
                videoRepository.save(video);
            } else {
                videoService.saveVideo(video);
            }

            return ResponseEntity.ok("Lesson và Video đã được cập nhật thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PostMapping("/updateLessonWithVideo")
    public ResponseEntity<?> updateLessonWithVideo(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("topic") String topic,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam(value = "documentShort", required = false) String documentShort,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "documentUrlFile", required = false) MultipartFile documentUrlFile
    ) {
        try {
            // Tìm Lesson và cập nhật thông tin
            Optional<Lesson> lessonOptional = lessonRepository.findById(id);
            if (!lessonOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson không tồn tại");
            }
            Lesson lesson = lessonOptional.get();
            lesson.setTitle(title);
            lesson.setTopic(topic);
            Optional<Course> course = courseRepository.findById(courseId);
            lesson.setCourse(course.get());
            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            lesson.setChapter(chapter.get());
            lesson.setUpdatedAt(LocalDateTime.now());
            lessonService.updateLessonAdmin(lesson);


            // Thêm Video mới cho Lesson
            Video video = new Video();
            video.setLesson(lesson);
            video.setTitle(videoTitle);
            video.setCreatedAt(LocalDateTime.now());
            video.setUpdatedAt(LocalDateTime.now());
            if (documentShort != null) {
                video.setDocumentShort(documentShort);
            }
            if (documentUrlFile != null) {
                videoService.uploadDocument(documentUrlFile);
            }


            String videoURL = videoService.uploadVideo(videoFile);
            video.setUrl(videoURL);
            videoRepository.save(video);
            return ResponseEntity.ok("Lesson và Video đã được cập nhật thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PutMapping("/updateLessonWithVideoOrDocument")
    public ResponseEntity<?> updateLessonWithVideoOrDocumentFile(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("topic") String topic,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoId") int videoId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam(value = "documentShort", required = false) String documentShort,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "documentUrlFile", required = false) MultipartFile documentUrlFile
    ) {
        try {
            // Tìm Lesson và cập nhật thông tin
            Optional<Lesson> lessonOptional = lessonRepository.findById(id);
            if (!lessonOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson không tồn tại");
            }
            Lesson lesson = lessonOptional.get();
            lesson.setTitle(title);
            Optional<Course> course = courseRepository.findById(courseId);
            lesson.setCourse(course.get());
            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            lesson.setChapter(chapter.get());
            lesson.setTopic(topic);
            lesson.setUpdatedAt(LocalDateTime.now());

            lessonService.updateLessonAdmin(lesson);

            Optional<Video> video1 = videoRepository.findById(videoId);
            if (!video1.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video không tồn tại");
            }
            Video video = new Video();
            video.setId(video1.get().getId());
            video.setLesson(lesson);
            video.setTitle(videoTitle);
            if (documentShort != null) {
                video.setDocumentShort(documentShort);
            }
            if (videoFile != null) {
                String videoURL = videoService.uploadVideo(videoFile);
                video.setUrl(videoURL);
            }
            if (documentUrlFile != null) {
                video.setDocumentShort(documentShort);
            }

            video.setCreatedAt(LocalDateTime.now());
            video.setUpdatedAt(LocalDateTime.now());
            video.setUrl(video1.get().getUrl());
            video.setDocumentUrl(video1.get().getDocumentUrl());

            videoService.uploadVideoUpdate(video, videoFile, documentUrlFile);

            return ResponseEntity.ok("Lesson và Video đã được cập nhật thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }


    @PutMapping("/updateLessonWithVideoAll")
    public ResponseEntity<?> updateLessonWithVideoOrDocumentFileAll(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("topic") String topic,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam(value = "videoId", required = false) String videoId,
            @RequestParam(value = "documentShort", required = false) String documentShort,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "documentUrlFile", required = false) MultipartFile documentUrlFile
    ) {
        try {
            // Tìm Lesson và cập nhật thông tin
            Optional<Lesson> lessonOptional = lessonRepository.findById(id);
            if (!lessonOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson không tồn tại");
            }
            Lesson lesson = lessonOptional.get();
            lesson.setTitle(title);
            Optional<Course> course = courseRepository.findById(courseId);
            lesson.setCourse(course.get());
            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            lesson.setChapter(chapter.get());
            lesson.setTopic(topic);
            lesson.setUpdatedAt(LocalDateTime.now());

            lessonService.updateLessonAdmin(lesson);

            if (videoId != null) {
                Video video = videoRepository.findById(Integer.parseInt(videoId)).get();
                if (video == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Video không tồn tại");
                }
                video.setLesson(lesson);
                video.setTitle(videoTitle);
                if (documentShort != null) {
                    video.setDocumentShort(documentShort);
                }
                if (videoFile != null) {
                    String videoURL = videoService.uploadVideo(videoFile);
                    video.setUrl(videoURL);
                }
                if (documentUrlFile != null) {
                    String documentURL = videoService.uploadDocument(documentUrlFile);
                    video.setDocumentUrl(documentURL);
                }
                if (video.getUpdatedAt() != null) {
                    video.setUpdatedAt(LocalDateTime.now());
                }
                if (video.getCreatedAt() != null) {
                    video.setCreatedAt(LocalDateTime.now());
                }
                videoRepository.saveAndFlush(video);
            } else {
                Video videoItem = new Video();
                videoItem.setLesson(lesson);
                videoItem.setTitle(videoTitle);

                if (documentShort != null) {
                    videoItem.setDocumentShort(documentShort);
                }
                if (videoFile != null) {
                    String url = videoService.uploadVideo(videoFile);
                    videoItem.setUrl(url);
                }
                if (documentUrlFile != null) {
                    String DocumentUrl = videoService.uploadDocument(documentUrlFile);
                    videoItem.setDocumentUrl(DocumentUrl);
                }
                videoItem.setCreatedAt(LocalDateTime.now());
                videoItem.setUpdatedAt(LocalDateTime.now());
                videoRepository.save(videoItem);
            }
            return ResponseEntity.ok("Lesson và Video đã được cập nhật thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<Page<AdminLessonGetDTO>> getLessonsWithCourseAndChapter(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminLessonGetDTO> lessons = lessonService.getLessonWithCourseAndChapter(page, size);
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/restore/list-all-lessons")
    public Page<AdminLesssonDTORestoreList> getLessons(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer chapterId,
            @RequestParam(required = false) String lessonTitle,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (lessonTitle.equals("")) {
            lessonTitle = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }

        return lessonService.getLessons(courseId, chapterId, lessonTitle, deletedDate, page, size);
    }

    @PutMapping("/restore/{lessonId}")
    public ResponseEntity<Lesson> restoreLesson(@PathVariable Integer lessonId) {
        AdminLesssonDTORestoreList lesssonDTORestoreList = new AdminLesssonDTORestoreList();
        lesssonDTORestoreList.setId(lessonId);
        Lesson restoreChapter = lessonService.updateRestoreLesson(lesssonDTORestoreList);
        return ResponseEntity.ok(restoreChapter);
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<?> LockLessonAdmin(@PathVariable int id) {
        try {
            Lesson lockLessonAdmin = lessonService.LockLessonAdmin(id);
            return ResponseEntity.ok().body("Lesson with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found with ID: " + id);
        }
    }

    @PutMapping("/unlock/{id}")
    public ResponseEntity<ApiResponse<Boolean>> UnlockLessonAdmin(@PathVariable int id) {
        try {
            Boolean unlockedLesson = lessonService.UnlockLessonAdmin(id);

            // Trả về phản hồi thành công với dữ liệu bài học đã mở khóa
            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Bài học đã được mở khóa thành công!",
                    unlockedLesson
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

    @PutMapping("/delete/{lessonId}")
    public ResponseEntity<?> deleteAccountAdmin(@PathVariable int lessonId) {
        try {
            Lesson deletedLesson = lessonService.deleteLessonAdmin(lessonId);
            return ResponseEntity.ok().body("Lesson with ID " + lessonId + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found with ID: " + lessonId);
        }
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeLessonAdmin(@PathVariable int id) {
        try {
            Lesson deletedLesson = lessonService.activeLessonAdmin(id);
            return ResponseEntity.ok().body("Lesson with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found with ID: " + id);
        }
    }

    @DeleteMapping("/delete/{lessonId}")
    public ResponseEntity<String> deleteLesson(@PathVariable Integer lessonId) {
        AdminLesssonDTORestoreList lesssonDTORestoreList = new AdminLesssonDTORestoreList();
        lesssonDTORestoreList.setId(lessonId);
        lessonService.deleteRestoreLesson(lesssonDTORestoreList);
        return ResponseEntity.ok("Lesson permanently deleted.");
    }

    @GetMapping("/test/lesson/{testId}")
    public ResponseEntity<ApiResponse<List<AdminLessonsDTOList>>> getLessonDetails(
            @PathVariable Integer testId,
            @RequestParam(required = false) Integer lessonId,
            @RequestParam(required = false) Integer chapterId
    ) {
        try {
            List<AdminLessonsDTOList> lessonDetails = lessonService.getLessonDetails(testId, lessonId, chapterId);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Lessons fetched successfully", lessonDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error fetching lessons", null));
        }
    }

    //Lấy ra các bài học chưa có bài test của chương - EMPTYTEST
    @GetMapping("/chapter/{chapterId}")
    public List<AdminLessonsDTOList> getLessonsByChapterId(@PathVariable Integer chapterId, @RequestParam(required = false) Integer courseId) {
        if (chapterId.equals(0)) {
            chapterId = null;
        }
        return lessonService.getLessonByChapterId(chapterId, courseId);
    }

    //Lấy ra các bài học  của chương
    @GetMapping("/chapter-all/{chapterId}")
    public Page<AdminLessonsDTOList> getLessonsByChapterIdPage(@PathVariable Integer chapterId,
                                                               @RequestParam(required = false) Integer courseId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        if (chapterId.equals(0)) {
            chapterId = null;
        }
        return lessonService.getLessonByChapterIdPage(chapterId, courseId, page, size);
    }

    //Cập nhật bài học có bài test hay không cần bài test - FULLTEST - NOTTEST
    @PutMapping("/excluded/{chapterId}")
    public Boolean updateExcludedChapterLesson(@PathVariable Integer chapterId,
                                               @RequestBody AdminLessonsDTOList[] adminLessonsDTOList) {
        if (chapterId.equals(0)) {
            chapterId = null;
        }

        return lessonService.updateExcludedChapterLesson(adminLessonsDTOList);
    }

    @PostMapping("/upload-video-lesson")
    public ResponseEntity<ApiResponse<Lesson>> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                           @RequestParam("chapterId") Integer chapterId) {
        try {
            Lesson unlockedLesson = new Lesson();
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                // Lưu file hoặc xử lý theo nhu cầu
                System.out.println("Uploading file: " + fileName + " for chapter: " + chapterId);
            }
            // Trả về phản hồi thành công với dữ liệu bài học đã mở khóa
            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Lesson unlocked successfully!",
                    unlockedLesson
            ));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ApiResponse<>(
                    e.getStatusCode().value(),
                    e.getReason(),
                    null
            ));
        }
    }

}
