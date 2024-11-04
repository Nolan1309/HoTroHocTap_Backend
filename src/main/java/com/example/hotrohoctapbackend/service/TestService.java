package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Test;
import com.example.hotrohoctapbackend.DTO.AdminTestUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service  // Thêm @Service để Spring quản lý lớp này như một Spring Bean
public class TestService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestRepository testRepository;
    @Transactional
    public Test addTest(AdminTestUpdateDTO newTestDTO) {
        // Khởi tạo một đối tượng Test mới
        Test test = new Test();

        // Cập nhật các trường
        test.setTitle(newTestDTO.getTitle());
        test.setDescription(newTestDTO.getDescription());
        test.setTotalQuestion(newTestDTO.getTotalQuestion());
        test.setIsSummary(newTestDTO.getIsSummary());

        if (newTestDTO.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(newTestDTO.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            test.setLesson(lesson);
        }

        if (newTestDTO.getChapterId() != null) {
            Chapter chapter = chapterRepository.findById(newTestDTO.getChapterId())
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
            test.setChapter(chapter);
        }

        if (newTestDTO.getCourseId() != null) {
            Course course = courseRepository.findById(newTestDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            test.setCourse(course);
        }

        // Lưu lại Test mới
        return testRepository.save(test);
    }
    @Transactional
    public Test updateTest(int id, AdminTestUpdateDTO updateDTO)    {
        // Lấy Test từ database
        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));

        // Cập nhật các trường
        if (updateDTO.getTitle() != null) {
            test.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getDescription() != null) {
            test.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(updateDTO.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Lesson not found"));
            test.setLesson(lesson);
        }

        if (updateDTO.getChapterId() != null) {
            Chapter chapter = chapterRepository.findById(updateDTO.getChapterId())
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
            test.setChapter(chapter);
        }

        if (updateDTO.getCourseId() != null) {
            Course course = courseRepository.findById(updateDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            test.setCourse(course);
        }

        if (updateDTO.getTotalQuestion() != null) {
            test.setTotalQuestion(updateDTO.getTotalQuestion());
        }

        if (updateDTO.getIsSummary() != null) {
            test.setIsSummary(updateDTO.getIsSummary());
        }

        // Lưu lại
        return testRepository.save(test);
    }

    public AdminTestUpdateDTO getTestById(int id) {
        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));

        // Chuyển đổi Test sang AdminTestResponseDTO
        AdminTestUpdateDTO responseDTO = new AdminTestUpdateDTO();
        responseDTO.setId(test.getId());
        responseDTO.setTitle(test.getTitle());
        responseDTO.setDescription(test.getDescription());
        if (test.getLesson() != null) {
            responseDTO.setLessonId(test.getLesson().getId());
        }
        if (test.getChapter() != null) {
            responseDTO.setChapterId(test.getChapter().getId());
        }
        if (test.getCourse() != null) {
            responseDTO.setCourseId(test.getCourse().getId());
        }
        responseDTO.setTotalQuestion(test.getTotalQuestion());
        responseDTO.setIsSummary(test.getIsSummary());

        return responseDTO;
    }
}
