package com.example.hotrohoctapbackend.controller;


import com.example.hotrohoctapbackend.DTO.VideoDTO_User;
import com.example.hotrohoctapbackend.entity.Video;
import com.example.hotrohoctapbackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "http://localhost:3000")
public class VideoController {

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
}