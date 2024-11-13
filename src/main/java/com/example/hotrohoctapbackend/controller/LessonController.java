package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.LessonDTOVideo_Admin;
import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Video;
import com.example.hotrohoctapbackend.service.LessonService;
import com.example.hotrohoctapbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/{id}")
    public List<LessonDTOVideo_Admin> getLessonByIdAdmin(@PathVariable int id) {
        return lessonService.getLessonVideoTestDataByLessonId(id);
    }

    @PostMapping("/updateLessonWithVideo")
    public ResponseEntity<?> updateLessonWithVideo(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("duration") Integer duration,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam("documentShort") String documentShort,
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
            lesson.setDuration(duration);

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
            video.setDocumentShort(documentShort);
            video.setDuration(duration); // Lưu duration của video
            video.setCreatedAt(LocalDateTime.now());
            video.setUpdatedAt(LocalDateTime.now());


            videoService.uploadVideo(video, videoFile, documentUrlFile);

            return ResponseEntity.ok("Lesson và Video đã được cập nhật thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    @PutMapping("/updateLessonWithVideoOrDocument")
    public ResponseEntity<?> updateLessonWithVideoOrDocumentFile(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("duration") Integer duration,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("videoId") int videoId,
            @RequestParam("videoTitle") String videoTitle,
            @RequestParam("documentShort") String documentShort,
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
            lesson.setDuration(duration);

            Optional<Course> course = courseRepository.findById(courseId);
            lesson.setCourse(course.get());

            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            lesson.setChapter(chapter.get());

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
            video.setDocumentShort(documentShort);
            video.setDuration(duration); // Lưu duration của video
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

}
