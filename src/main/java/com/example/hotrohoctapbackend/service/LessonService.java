package com.example.hotrohoctapbackend.service;
import com.example.hotrohoctapbackend.DTO.LessonDTO2;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    public Lesson addLesson(LessonDTO2 lessonDTO2) {
        // Tìm chapter dựa trên chapter_id
        Chapter chapter = chapterRepository.findById(lessonDTO2.getChapter_id())
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        // Tạo lesson mới và gán các thuộc tính từ DTO
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonDTO2.getTitle());
        lesson.setChapter(chapter); // Gán chapter tìm được

        // Thiết lập CreatedAt và UpdatedAt
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreatedAt(now);
        lesson.setUpdatedAt(now); // Khi tạo mới, CreatedAt và UpdatedAt sẽ giống nhau

        // Lưu lesson vào cơ sở dữ liệu
        return lessonRepository.save(lesson);
    }
}
