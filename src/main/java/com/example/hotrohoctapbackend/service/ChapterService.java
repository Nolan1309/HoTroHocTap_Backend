package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.ChapterDTO;
import com.example.hotrohoctapbackend.DTO.LessonDTO;
import com.example.hotrohoctapbackend.DTO.TestDTO;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:3000")
@Service
public class ChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseRepository courseRepository;
    // Phương thức tìm Chapter bằng courseId và trả về danh sách ChapterDTO
    public List<ChapterDTO> findChaptersByCourseId(Integer courseId) {
        // Tìm chapters bằng courseId
        List<Chapter> chapters = chapterRepository.findByCourseId(courseId);

        // Chuyển đổi chapter entity thành ChapterDTO
        return chapters.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Phương thức chuyển đổi từ Chapter entity sang ChapterDTO
    private ChapterDTO convertToDTO(Chapter chapter) {
        return new ChapterDTO(
                chapter.getId(),
                chapter.getTitle(),
                chapter.getLessonList().stream().map(lesson -> new LessonDTO(lesson.getId(), lesson.getTitle())).collect(Collectors.toList()), // Convert lessonList
                chapter.getTestList().stream().map(test -> new TestDTO(test.getId(), test.getTitle(), test.getDescription(), test.getIsSummary(), test.getTotalQuestion(), test.getCreatedAt(), test.getUpdatedAt(), test.getLesson().getId(), test.getChapter().getId())).collect(Collectors.toList()),
                chapter.getCourse().getId() // Lấy id_course
        );
    }
    public Chapter addChapter(ChapterDTO chapterDTO) {
        // Tìm course theo id_course trong DTO
        Course course = courseRepository.findById(chapterDTO.getId_course())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + chapterDTO.getId_course()));

        // Tạo mới Chapter từ ChapterDTO
        Chapter chapter = new Chapter();
        chapter.setTitle(chapterDTO.getTitle());
        chapter.setCourse(course);

        // Lưu chapter vào database
        return chapterRepository.save(chapter);
    }
}
