package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestUpdateDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminUpdateTestToLesson;
import com.example.hotrohoctapbackend.DTO.User.TestDTO_User;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseRepository courseRepository;

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

    @Transactional
    public Test addTest(@NotNull AdminTestUpdateDTO newTestDTO) {
        // Khởi tạo một đối tượng Test mới
        Test test = new Test();

        // Cập nhật các trường
        test.setTitle(newTestDTO.getTitle());
        test.setDescription(newTestDTO.getDescription());
        test.setTotalQuestion(newTestDTO.getTotalQuestion());
        test.setSummary(newTestDTO.getIsSummary());
        test.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        test.setLesson(null);


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
    public Test updateTest(int id, AdminTestUpdateDTO updateDTO) {
        // Lấy Test từ database
        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
        test.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        // Cập nhật các trường
        if (updateDTO.getTitle() != null) {
            test.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getDescription() != null) {
            test.setDescription(updateDTO.getDescription());
        }

        test.setLesson(null);


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
            test.setSummary(updateDTO.getIsSummary());
        }

        // Lưu lại
        return testRepository.save(test);
    }
    @Transactional
    public Test updateTestToLesson(int id, AdminUpdateTestToLesson updateDTO) {
        // Lấy Test từ database
        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));

        if (updateDTO.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(updateDTO.getLessonId())
                    .orElseThrow(() -> new RuntimeException("Chapter not found"));
            test.setLesson(lesson);
        }

        // Lưu lại
        return testRepository.save(test);
    }
    public AdminTestUpdateDTO getTestByIdAdmin(int id) {
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
        responseDTO.setIsSummary(test.isSummary());

        return responseDTO;
    }

    public Test addTestToLessonAdmin(Test test) {
        return testRepository.saveAndFlush(test);
    }

    public List<AdminTestGetDTO> getAllTestSummariesAdmin() {
        return testRepository.findAllTestSummaries().stream()
                .map(result -> new AdminTestGetDTO(
                        (Integer) result[0],                // id
                        (String) result[1],                 // title
                        (Integer) result[2],                // totalQuestion
                        (Date) result[3],                    // createdAt
                        (Boolean) result[4]                // createdAt
                ))
                .collect(Collectors.toList());
    }


    public Test deleteTestAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Test> accountOpt = testRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Test account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return testRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + testID);
        }
    }

    public Test activeTestAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Test> accountOpt = testRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Test account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return testRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }
}
