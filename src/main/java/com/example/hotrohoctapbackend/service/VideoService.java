package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.VideoDTO_User;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Video;
import com.example.hotrohoctapbackend.util.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private FirebaseStorageService firebaseFileService;

    public VideoDTO_User getVideoById(int id) {
        Optional<Video> video = videoRepository.findById(id);
        return video.map(this::convertToDTO).orElse(null);
    }

    public VideoDTO_User getFirstVideoByCourseId(int courseId) {
        Video video = videoRepository.findFirstVideoByCourseId(courseId);
        return video != null ? convertToDTO(video) : null;
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

    public Video uploadVideo(Video videoInput, MultipartFile videoFile, MultipartFile document) throws IOException {
        Video video = firebaseFileService.saveTest(videoInput, videoFile, document);
        return videoRepository.save(video);
    }

    public Video uploadVideoUpdate(Video videoInput, MultipartFile videoFile, MultipartFile document) throws IOException {

        if (videoFile != null && document == null) {
            String videoURL = firebaseFileService.saveTestVideo(videoFile);
            videoInput.setUrl(videoURL);
            return videoRepository.saveAndFlush(videoInput);
        }
        if (videoFile == null && document != null) {
            String documentURl = firebaseFileService.saveTestDocument(document);
            videoInput.setDocumentUrl(documentURl);
            return videoRepository.saveAndFlush(videoInput);
        }

        if (videoFile == null && document == null) {

            return videoRepository.saveAndFlush(videoInput);
        }
        int i = 1;
        Video video = firebaseFileService.saveTest(videoInput, videoFile, document);
        return videoRepository.save(video);
    }

    //    //VIDEO
//    public Video uploadVideo(String title, String description, MultipartFile file) throws IOException {
//        Video video = firebaseFileService.saveTest(file, title, description);
//        return videoRepository.save(video);
//    }
    public void deleteVideo(int id) {
        Video video = videoRepository.findById(id).orElse(null);
        String path = firebaseFileService.extractFilePathVideo(video.getUrl());
        System.out.println(path);
        if (video != null) {
            try {
                // Xóa video khỏi Firebase Storage
                firebaseFileService.deleteVideoServerVideo(path);
                // Xóa video khỏi cơ sở dữ liệu
                videoRepository.deleteById(id);
            } catch (Exception e) {
                System.err.println("Error deleting video with ID " + id + ": " + e.getMessage());
                throw new RuntimeException("Failed to delete video", e);
            }
        } else {
            System.out.println("Video with ID " + id + " not found.");
        }
    }
    public List<Video> getAllVideosByCourseId(Integer courseId) {
        return videoRepository.findVideosByCourseId(courseId);
    }
    public Video updateVideoStatus(Integer videoId, Boolean isViewTest) {
        Optional<Video> videoOpt = videoRepository.findById(videoId);
        if (videoOpt.isPresent()) {
            Video video = videoOpt.get();
            video.setIsViewTest(isViewTest);  // Cập nhật trạng thái
            return videoRepository.save(video); // Lưu thay đổi vào DB
        } else {
            return null; // Không tìm thấy video
        }
    }
}
