package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.VideoDTO_User;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    public VideoDTO_User getVideoById(int id) {
        Optional<Video> video = videoRepository.findById(id);
        return video.map(this::convertToDTO).orElse(null);
    }
    // Chuyển đổi từ Video sang VideoDTO
    private VideoDTO_User convertToDTO(Video video) {
        VideoDTO_User videoDTO = new VideoDTO_User();
        videoDTO.setId(video.getId());
        videoDTO.setTitle(video.getTitle());
        videoDTO.setUrl(video.getUrl());
        videoDTO.setDocumentShort(video.getDocumentShort());
        videoDTO.setDocumentUrl(video.getDocumentUrl());
        videoDTO.setDuration(video.getDuration());
        Lesson lesson = video.getLesson();
        videoDTO.setLesson_id(lesson.getId());

        return videoDTO;
    }
}
