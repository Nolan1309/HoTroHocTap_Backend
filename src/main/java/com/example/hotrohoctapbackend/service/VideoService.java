package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV2.AdminVideoDTOEditCourseList;
import com.example.hotrohoctapbackend.DTO.AdminV3.Video.VideoDTO;
import com.example.hotrohoctapbackend.DTO.User.VideoDTO_User;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.VideoRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.util.FirebaseStorageService;
import jakarta.transaction.Transactional;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestService testService;

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonService lessonService;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private FirebaseStorageService firebaseFileService;

    public List<VideoDTO> getVideosByCourseId(int courseId) {
        List<Video> videos = videoRepository.findByCourseIdAndIsDeletedFalse(courseId);
        return videos.stream()
                .map(video -> new VideoDTO(String.valueOf(video.getId()), String.valueOf(video.getLesson().getId()), video.getTitle(),
                        video.getUrl(), video.getDocumentShort(), video.getDocumentUrl(), video.getDuration(),
                        video.getCreatedAt().toString(), video.getUpdatedAt().toString(),
                        video.getDeletedDate() != null ? video.getDeletedDate().toString() : "", video.isDeleted(), video.getIsViewTest()))
                .collect(Collectors.toList());
    }

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

    public Video addVideo(Integer lessonId, String title, MultipartFile file, MultipartFile documentFile, String documentShort, Boolean isViewTest) throws IOException {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Video video = new Video();
        video.setTitle(title);
        video.setLesson(lesson);
        video.setDocumentShort(documentShort);
        video.setIsViewTest(isViewTest);
        video.setCreatedAt(LocalDateTime.now());
        video.setUpdatedAt(LocalDateTime.now());
        video.setDeleted(false);
        video.setDeletedDate(LocalDateTime.now());

        String urlVideo = firebaseFileService.saveVideo(file);
        video.setUrl(urlVideo);
        String documentUrl = firebaseFileService.uploadFileDocumentForCourse(documentFile);
        video.setDocumentUrl(documentUrl);

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(urlVideo);
        grabber.start();
        double durationInSeconds = grabber.getLengthInTime() / 1000000.0;
        int duration = (int) durationInSeconds;

        video.setDuration(duration);

        return videoRepository.save(video);
    }

    public Video updateVideo(Integer videoId, Integer lessonId, String title, MultipartFile file, MultipartFile documentFile, String documentShort, Boolean isViewTest) throws IOException {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        video.setTitle(title);
        video.setLesson(lesson);
        video.setDocumentShort(documentShort);
        video.setIsViewTest(isViewTest);
        video.setUpdatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String urlVideo = firebaseFileService.saveVideo(file);
            video.setUrl(urlVideo);
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(urlVideo);
            grabber.start();
            double durationInSeconds = grabber.getLengthInTime() / 1000000.0;
            int duration = (int) durationInSeconds;
            video.setDuration(duration);
        }
        if (documentFile != null && !documentFile.isEmpty()) {
            String documentUrl = firebaseFileService.uploadFileDocumentForCourse(documentFile);
            video.setDocumentUrl(documentUrl);
        }
        return videoRepository.save(video);
    }

    public String uploadVideo(MultipartFile videoFile) throws IOException {
        return firebaseFileService.saveVideo(videoFile);
    }

    public String uploadDocument(MultipartFile fileDocx) throws IOException {
        return firebaseFileService.saveTestDocument(fileDocx);
    }

    @Transactional
    public Video saveVideo(Video videoInput) {
        return videoRepository.save(videoInput);
    }

    public void uploadVideoAllItem(MultipartFile[] videoFiles, Integer courseId, Integer chapterId) throws IOException {
        try {
            int durationTotal = 0;
            Course course = courseRepository.findById((courseId)).get();
            for (MultipartFile file : videoFiles) {
                String urlFile = firebaseFileService.saveTestVideo(file);
                String fileName = file.getOriginalFilename();
                if (fileName != null) {
                    fileName = fileName.replaceAll("\\.mp4$", "");  // Loại bỏ ".mp4" nếu có ở cuối
                }
                FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(urlFile);
                grabber.start();
                double durationInSeconds = grabber.getLengthInTime() / 1000000.0;
                int duration = (int) durationInSeconds;


                Lesson lesson = new Lesson();
                lesson.setTitle(fileName);
                lesson.setTopic(fileName);
                lesson.setCreatedAt(LocalDateTime.now());
                lesson.setUpdatedAt(LocalDateTime.now());
                lesson.setDeletedDate(LocalDateTime.now());
                lesson.setIsTestExcluded("FULLTEST");
                lesson.setDuration(duration);
                durationTotal = duration + durationTotal;
                lesson.setCourse(courseRepository.findById(courseId).get());
                lesson.setChapter(chapterRepository.findById(chapterId).get());
                Lesson lessonSave = lessonService.saveLesson(lesson);

                Test itemTest = new Test();
                itemTest.setLesson(lessonSave);
                itemTest.setAssigned(true);
                itemTest.setCourse(course);
                itemTest.setChapter(chapterRepository.findById(chapterId).get());
                itemTest.setSummary(false);
                itemTest.setDuration(300);
                itemTest.setCreatedAt(new Date());
                itemTest.setUpdatedAt(new Date());

                itemTest.setFormat("test");
                itemTest.setTotalQuestion(5);
                itemTest.setEasyQuestion(2);
                itemTest.setMediumQuestion(2);
                itemTest.setHardQuestion(1);
                itemTest.setType("multiple-choice");
                itemTest.setTitle(fileName);

                Test testSave = testService.saveTest((itemTest));


                Video itemVideo = new Video();
                itemVideo.setTitle(fileName);
                itemVideo.setCreatedAt(LocalDateTime.now());
                itemVideo.setUpdatedAt(LocalDateTime.now());
                itemVideo.setUrl(urlFile);
                itemVideo.setLesson(lessonSave);


                itemVideo.setDuration(duration);
                Video videoSave = videoRepository.save((itemVideo));
                grabber.stop();


            }
            course.setDuration(durationTotal);
            courseRepository.saveAndFlush(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        String videoURL = firebaseFileService.saveVideo(videoFile);
        videoInput.setUrl(videoURL);
        return videoRepository.save(videoInput);
    }

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

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public List<AdminVideoDTOEditCourseList> getAllVideosByCourseId(Integer courseId) {

        List<Object[]> result = videoRepository.findVideosByCourseId(courseId);
        List<AdminVideoDTOEditCourseList> adminVideoDTOEditCourseListList = new ArrayList<>();
        for (Object[] item : result) {
            AdminVideoDTOEditCourseList adminVideoDTOEditCourseList = new AdminVideoDTOEditCourseList();
            adminVideoDTOEditCourseList.setId((Integer) item[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(item[1]);
            adminVideoDTOEditCourseList.setCreatedAt(createAt);

            LocalDateTime deletedDate = convertTimestampToLocalDateTime(item[2]);
            adminVideoDTOEditCourseList.setDeletedDate(deletedDate);

            adminVideoDTOEditCourseList.setDocumentShort((String) item[3]);
            adminVideoDTOEditCourseList.setDocumentUrl((String) item[4]);
            adminVideoDTOEditCourseList.setDuration((Integer) item[5]);
            adminVideoDTOEditCourseList.setDeleted((Boolean) item[6]);
            adminVideoDTOEditCourseList.setVideoTitle((String) item[7]);

            LocalDateTime updatedAt = convertTimestampToLocalDateTime(item[8]);
            adminVideoDTOEditCourseList.setUpdatedAt(updatedAt);


            adminVideoDTOEditCourseList.setVideoUrl((String) item[9]);
            adminVideoDTOEditCourseList.setLessonId((Integer) item[10]);
            adminVideoDTOEditCourseList.setViewTest((Boolean) item[11]);
            adminVideoDTOEditCourseListList.add(adminVideoDTOEditCourseList);
        }

        return adminVideoDTOEditCourseListList;
    }

    public Boolean updateVideoStatus(Integer videoId, Boolean isViewTest) {
        Optional<Video> videoOpt = videoRepository.findById(videoId);
        if (videoOpt.isPresent()) {
            int check = videoRepository.updateVideoStatus(videoId, isViewTest);
            if (check != 0) {
                return true;
            } else return false;

        } else {
            return null; // Không tìm thấy video
        }
    }

    public Map<String, Integer> getLessonAndChapterIdByVideoId(int videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        Lesson lesson = video.getLesson();
        Chapter chapter = lesson.getChapter();

        Map<String, Integer> ids = new HashMap<>();
        ids.put("lessonId", lesson.getId());
        ids.put("chapterId", chapter.getId());
        return ids;
    }


}
