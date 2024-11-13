package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.ChapterDTOAdmin;
import com.example.hotrohoctapbackend.DTO.ChapterDTO;

import com.example.hotrohoctapbackend.DTO.LessonDTO;
import com.example.hotrohoctapbackend.DTO.TestDTO;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Test;
import com.google.common.collect.FluentIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TestRepository testRepository;

    // Phương thức tìm Chapter bằng courseId và trả về danh sách ChapterDTO
    public List<ChapterDTO> findChaptersByCourseId(Integer courseId) {
        // Tìm chapters bằng courseId
        List<Chapter> chapters = chapterRepository.findByCourseId(courseId);
        List<ChapterDTO> chapterDTOList = new ArrayList<>();

        for (Chapter item : chapters) {
            ChapterDTO chapterDTO = new ChapterDTO();

            List<LessonDTO> lessonDTOS = new ArrayList<>();
            List<TestDTO> testDTOS = new ArrayList<>();

            List<Lesson> lessonList = lessonRepository.findLessonsByChapterId(item.getId());
            List<Test> testList = testRepository.findTestsByChapterId(item.getId());

            for (Lesson lessonDTO : lessonList) {
                LessonDTO lessonDTO1 = new LessonDTO();
                lessonDTO1.setId(lessonDTO.getId());
                lessonDTO1.setTitle(lessonDTO.getTitle());
                lessonDTOS.add(lessonDTO1);
            }

            for (Test test : testList) {
                TestDTO testDTO = new TestDTO();
                testDTO.setId(test.getId());
                testDTO.setTitle(test.getTitle());
                testDTO.setDescription(test.getDescription());
                testDTO.setSummary(test.isSummary());
                testDTO.setTotalQuestion(test.getTotalQuestion());
                testDTO.setCreatedAt(test.getCreatedAt());
                testDTO.setUpdatedAt(test.getUpdatedAt());
                testDTO.setLessonId(test.getLesson() != null ? test.getLesson().getId() : null); // Kiểm tra lesson null
                testDTO.setChapterId(test.getChapter() != null ? test.getChapter().getId() : null);
                testDTOS.add(testDTO);
            }

            chapterDTO.setId(item.getId());
            chapterDTO.setTitle(item.getTitle());
            chapterDTO.setLessonList(lessonDTOS);
            chapterDTO.setTestList(testDTOS);
            chapterDTO.setId_course(item.getCourse().getId());
            chapterDTOList.add(chapterDTO);
        }

        return chapterDTOList;

    }

    public List<ChapterDTOAdmin> findAllChapters()
    {
        List<Chapter> chapters = chapterRepository.findAll();

        List<ChapterDTOAdmin> chapterDTOAdmins = new ArrayList<>();
        for (Chapter item : chapters){
            ChapterDTOAdmin chapterDTOAdmin = new ChapterDTOAdmin();
            chapterDTOAdmin.setId(item.getId());
            chapterDTOAdmin.setTitle(item.getTitle());
            chapterDTOAdmin.setCourse_id(item.getCourse().getId());
            chapterDTOAdmin.setDeleted(item.isDeleted());
            chapterDTOAdmins.add(chapterDTOAdmin);
        }
        return chapterDTOAdmins;
    }

//    private ChapterDTO convertToDTO(Chapter chapter) {
//        return new ChapterDTO(
//                chapter.getId(),
//                chapter.getTitle(),
//                chapter.getLessonList().stream().map(lesson -> new LessonDTO(lesson.getId(), lesson.getTitle())).collect(Collectors.toList()), // Convert lessonList
//                chapter.getTestList().stream().map(test -> new TestDTO(test.getId(), test.getTitle(), test.getDescription(), test.isSummary(), test.getTotalQuestion(), test.getCreatedAt(), test.getUpdatedAt(), test.getLesson().getId(), test.getChapter().getId())).collect(Collectors.toList()),
//                chapter.getCourse().getId() // Lấy id_course
//        );
//    }

    // Phương thức chuyển đổi từ Chapter entity sang ChapterDTO
//    private ChapterDTO convertToDTO(Chapter chapter) {
//        return new ChapterDTO(
//                chapter.getId(),
//                chapter.getTitle(),
//                chapter.getLessonList().stream().map(lesson -> new LessonDTO(lesson.getId(), lesson.getTitle())).collect(Collectors.toList()), // Convert lessonList
//                chapter.getTestList().stream().map(test -> new TestDTO(test.getId(), test.getTitle(), test.getDescription(), test.isSummary(), test.getTotalQuestion(), test.getCreatedAt(), test.getUpdatedAt(), test.getLesson().getId(), test.getChapter().getId())).collect(Collectors.toList()),
//                chapter.getCourse().getId() // Lấy id_course
//        );
//    }
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