package com.example.hotrohoctapbackend.controller;


import com.example.hotrohoctapbackend.DTO.AdminV2.AdminVideoDTOEditCourseList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Video.VideoDTO;
import com.example.hotrohoctapbackend.DTO.User.VideoDTO_User;
import com.example.hotrohoctapbackend.entity.Video;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
public class
VideoController {

    @Autowired
    private VideoService videoService;

    // Tìm video theo ID
    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO_User> getVideoById(@PathVariable int id) {
        VideoDTO_User video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ApiResponse<List<VideoDTO>> getVideos(@PathVariable int courseId) {
        List<VideoDTO> videos = videoService.getVideosByCourseId(courseId);
        return new ApiResponse<>(200, "Success", videos);
    }

    @GetMapping("/view-user/{id}")
    public ResponseEntity<VideoDTO_User> getVideoByIdViewUser(@PathVariable int id) {
        VideoDTO_User video = videoService.getVideoById(id);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/first-video/{courseId}")
    public ResponseEntity<VideoDTO_User> getFirstVideoByCourseId(@PathVariable int courseId) {
        VideoDTO_User videoDTO = videoService.getFirstVideoByCourseId(courseId);
        if (videoDTO != null) {
            return new ResponseEntity<>(videoDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }


    //ADMIN
    @PostMapping("/uploadVideo")
    public ResponseEntity<Video> uploadVideoAdmin(
            @RequestParam("lesson_id") Integer lesson_id,
            @RequestParam("video_title") String video_title,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentFile") MultipartFile documentFile,
            @RequestParam("documentShort") String documentShort,
            @RequestParam("isViewTest") Boolean isViewTest
    ) {

        try {
            Video video = videoService.addVideo(lesson_id, video_title, file, documentFile, documentShort, isViewTest);
            return ResponseEntity.ok(video);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{videoId}")
    public ResponseEntity<Video> updateVideoAdmin(
            @PathVariable Integer videoId,
            @RequestParam("lesson_id") Integer lesson_id,
            @RequestParam("video_title") String video_title,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart(value = "documentFile", required = false) MultipartFile documentFile,
            @RequestParam("documentShort") String documentShort,
            @RequestParam("isViewTest") Boolean isViewTest
    ) {


        try {
            Video video = videoService.updateVideo(videoId, lesson_id, video_title, file, documentFile, documentShort, isViewTest);
            return ResponseEntity.ok(video);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoAdmin(@PathVariable int id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list/{courseId}")
    public List<AdminVideoDTOEditCourseList> getAllVideos(@PathVariable Integer courseId) {
        return videoService.getAllVideosByCourseId(courseId);
    }

    @PutMapping("/viewtest/{videoId}")
    public ResponseEntity<Boolean> updateVideoStatus(@PathVariable Integer videoId, @RequestBody Map<String, Boolean> requestBody) {
        Boolean isViewTest = requestBody.get("isViewTest");
        Boolean updatedVideo = videoService.updateVideoStatus(videoId, isViewTest);

        if (updatedVideo != null) {
            return ResponseEntity.ok(updatedVideo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Video không tìm thấy
        }
    }

    @GetMapping("/{videoId}/lesson-chapter-ids")
    public ResponseEntity<Map<String, Integer>> getLessonAndChapterId(@PathVariable int videoId) {
        Map<String, Integer> ids = videoService.getLessonAndChapterIdByVideoId(videoId);
        return ResponseEntity.ok(ids);
    }
}