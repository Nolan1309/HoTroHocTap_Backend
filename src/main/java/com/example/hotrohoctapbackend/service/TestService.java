package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.User.TestDTO_User;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.Chapter;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Lesson;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public TestDTO_User getTestById(int id) {
        Optional<Test> item = testRepository.findById(id);

        if (item.isPresent()) {
            return convertDTO(item.get());
        } else {
            // Xử lý trường hợp không tìm thấy Test, có thể trả về null hoặc ném ngoại lệ tùy theo yêu cầu của bạn
            throw new RuntimeException("Test not found with id: " + id);
        }
    }

    private TestDTO_User convertDTO(Test test) {
        TestDTO_User testDTOUser = new TestDTO_User();
        testDTOUser.setId(test.getId());
        Course course = test.getCourse();
        if (course != null) {
            testDTOUser.setCourse_id(course.getId());
        }

        // Kiểm tra Chapter có null không trước khi gán giá trị
        Chapter chapter = test.getChapter();
        if (chapter != null) {
            testDTOUser.setChapter_id(chapter.getId());
        }

        // Kiểm tra Lesson có null không trước khi gán giá trị
        Lesson lesson = test.getLesson();
        if (lesson != null) {
            testDTOUser.setLesson_id(lesson.getId());
        }

        testDTOUser.setTitle(test.getTitle());
        testDTOUser.setDescription(test.getDescription());
        testDTOUser.setSummary(test.isSummary());
        testDTOUser.setTotalQuestion(test.getTotalQuestion());
        testDTOUser.setCreatedAt(test.getCreatedAt());
        testDTOUser.setUpdatedAt(test.getUpdatedAt());
        return testDTOUser;
    }


}
