package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestUpdateDTO;
import com.example.hotrohoctapbackend.DTO.User.QuestionDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestDTO_User;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.service.QuestionService;
import com.example.hotrohoctapbackend.service.RedisTestService;
import com.example.hotrohoctapbackend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private RedisTestService redisTestService;
    @Autowired
    private TestService testService;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private LessonRepository lessonRepository;

    // Endpoint để lấy dữ liệu từ cache
    @GetMapping("/cache")
    public String getCache(@RequestParam String key) {
        return redisTestService.getFromCache(key);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestDTO_User> getTestById(@PathVariable int id) {
        try {
            TestDTO_User testDTO = testService.getTestById(id);
            return ResponseEntity.ok(testDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{testId}/questions")
    public ResponseEntity<List<QuestionDTO_User>> getQuestionsByTestId(@PathVariable int testId) {
        List<QuestionDTO_User> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable int id, @RequestBody AdminTestUpdateDTO updateDTO) {
        Test updatedTest = testService.updateTest(id, updateDTO);
        return ResponseEntity.ok(updatedTest);
    }

    @GetMapping("/chitiet/{id}")
    public ResponseEntity<AdminTestUpdateDTO> getTestByIdAdmin(@PathVariable int id) {
        AdminTestUpdateDTO responseDTO = testService.getTestByIdAdmin(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getall")
    public List<AdminTestGetDTO> getTestSummaries() {
        return testService.getAllTestSummariesAdmin();
    }

    @PostMapping("/add")
    public ResponseEntity<Test> addTest(@RequestBody AdminTestUpdateDTO newTestDTO) {
        try {
            Test newTest = testService.addTest(newTestDTO);
            return new ResponseEntity<>(newTest, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateLessonWithTest")
    public ResponseEntity<?> updateLessonWithTest(
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("duration") Integer duration,
            @RequestParam("chapterId") int chapterId,
            @RequestParam("courseId") int courseId,
            @RequestParam("testId") int testId,
            @RequestParam("titleTest") String titleTest

    ) {
        try {
            // Tìm Lesson và cập nhật thông tin
            Optional<Test> lessonOptional = testRepository.findById(testId);
            if (!lessonOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Test không tồn tại");
            }
            Test test = lessonOptional.get();


            test.setTitle(titleTest);
            Optional<Course> course = courseRepository.findById(courseId);
            test.setCourse(course.get());

            Optional<Chapter> chapter = chapterRepository.findById(chapterId);
            test.setChapter(chapter.get());

            Optional<Lesson> lesson = lessonRepository.findById(id);
            test.setLesson(lesson.get());

            test.setUpdatedAt(new Date());
            test.setSummary(false);

            testService.addTestToLessonAdmin(test);


            return ResponseEntity.ok("Test đã được thêm vào Lesson thành công");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra: " + e.getMessage());
        }
    }
    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteTestAdmin(@PathVariable int id) {
        try {
            Test deletedTest = testService.deleteTestAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeQuestionAdmin(@PathVariable int id) {
        try {
            Test activedTest = testService.activeTestAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

}
