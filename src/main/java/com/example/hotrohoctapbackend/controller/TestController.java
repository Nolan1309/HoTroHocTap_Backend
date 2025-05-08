package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO_Version2;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestUpdateDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminUpdateTestToLesson;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.ExamDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.ExamDetailDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.TestWithExamInfoDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Test.TestDTO;
import com.example.hotrohoctapbackend.DTO.User.QuestionDTO_User;
import com.example.hotrohoctapbackend.DTO.User.TestDTO_User;
import com.example.hotrohoctapbackend.DTO.User.UserQuestionExamDTO;
import com.example.hotrohoctapbackend.dao.ChapterRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.LessonRepository;
import com.example.hotrohoctapbackend.dao.TestRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.QuestionService;
import com.example.hotrohoctapbackend.service.RedisTestService;
import com.example.hotrohoctapbackend.service.TestService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
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

    @GetMapping("/course/{courseId}")
    public ApiResponse<List<TestDTO>> getTests(@PathVariable int courseId) {
        List<TestDTO> tests = testService.getTestsByCourseId(courseId);
        return new ApiResponse<>(200, "Success", tests);
    }

    @GetMapping("/course/availible/{courseId}")
    public ApiResponse<List<TestDTO>> getTestsAvalible(@RequestParam int chapterId, @PathVariable int courseId) {
        List<TestDTO> tests = testService.getTestsByCourseIdAndChapterId(chapterId, courseId);
        return new ApiResponse<>(200, "Success", tests);
    }

    @GetMapping("/{testId}/questions")
    public ResponseEntity<List<QuestionDTO_User>> getQuestionsByTestId(@PathVariable int testId) {
        List<QuestionDTO_User> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/{testId}/questions-exam")
    public ResponseEntity<List<UserQuestionExamDTO>> getQuestionsByTestId_Exam(@PathVariable int testId) {
        List<UserQuestionExamDTO> questions = questionService.getQuestionsByTestId_Exam(testId);
        return ResponseEntity.ok(questions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTest(@PathVariable int id, @RequestBody AdminTestGetListDTO_V2 updateDTO) {
        try {
            Boolean updatedTest = testService.updateTest(id, updateDTO);
            return ResponseEntity.ok(updatedTest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi cập nhật bài kiểm tra.");
        }
    }


    @GetMapping("/chitiet/{id}")
    public ResponseEntity<AdminTestGetListDTO_V2> getTestByIdAdmin(@PathVariable int id) {
        AdminTestGetListDTO_V2 responseDTO = testService.getTestByIdAdmin(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/exam/public")
    public ApiResponse<Page<ExamDTOPublic>> getTests(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ExamDTOPublic> exams = testService.getTestsByCourseAndTitle(courseId, title, accountId, page, size);
        return new ApiResponse<>(200, "Successfully fetched tests", exams);
    }

    @GetMapping("/exam/public/{testId}")
    public ApiResponse<ExamDetailDTOPublic> getTestDetails(@PathVariable Integer testId,
                                                           @RequestParam(required = false) Integer accountId) {
        try {
            ExamDetailDTOPublic examDTO = testService.getTestDetails(testId, accountId);
            return new ApiResponse<>(200, "Successfully fetched test details", examDTO);
        } catch (NoSuchElementException e) {
            return new ApiResponse<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    @GetMapping("/getall")
    public Page<AdminTestGetDTO_Version2> getAllTestSummariesAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return testService.getAllTestSummariesAdmin(page, size);
    }

    //Get danh sách Admin test
    @GetMapping("/filter-all")
    public ResponseEntity<Page<AdminTestGetListDTO_V2>> searchTests(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<AdminTestGetListDTO_V2> tests = testService.getAllTestSummariesAdmin_V2(courseId, title, pageable);
        return ResponseEntity.ok(tests);
    }

    //Get danh sách  ( thi thử )
    @GetMapping("/filter-all-exam")
    public ResponseEntity<ApiResponse<Page<TestWithExamInfoDTO>>> getTests(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<TestWithExamInfoDTO> result = testService.getFilteredTests(title, courseId, pageable);
            return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/filter-all-exam-list")
    public ResponseEntity<ApiResponse<List<TestWithExamInfoDTO>>> getExamsList(
            @RequestParam(required = false) Integer courseId
    ) {
        try {
            List<TestWithExamInfoDTO> result = testService.getFilteredExamList(courseId);
            return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi: " + e.getMessage(), null));
        }
    }

    @GetMapping("/detail-course-test")
    public ResponseEntity<AdminTestCheckInfoCourse> detailsCourseTest(@RequestParam(required = false) Integer courseId) {
        if (courseId == null) {
            // Nếu courseId không được cung cấp, trả về lỗi 400 với thông báo
            return ResponseEntity.badRequest().body(null);
        }
        AdminTestCheckInfoCourse tests = testService.getChapterAndLessonSummary(courseId);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/getall-list")
    public List<AdminTestGetDTO> getAllTestSummariesAdmin() {
        return testService.getAllTestSummariesAdminList();
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AdminTestAddDTO_V2>> addTest(@RequestBody AdminTestAddDTO_V2 newTestDTO) {
        try {
            Test newTest = testService.addTest(newTestDTO);
            if (newTest != null) {
                // Trả về thông tin bài kiểm tra vừa tạo
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Test created successfully", newTestDTO), HttpStatus.CREATED);
            }
            // Trả về thông tin bài kiểm tra vừa tạo
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Test created fail", null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Xử lý lỗi chi tiết
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error creating test: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/add-exam")
    public ResponseEntity<ApiResponse<TestWithExamInfoDTO>> addTest(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Integer totalQuestion,
            @RequestParam(required = false) Integer easyQuestion,
            @RequestParam(required = false) Integer mediumQuestion,
            @RequestParam(required = false) Integer hardQuestion,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) Integer point,
            @RequestParam Integer courseId,
            // ExamInfo
            @RequestParam(required = false) String intro,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String cost,
            @RequestParam(required = false) String examType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestPart(value = "image", required = false) MultipartFile[] image
    ) {
        try {
            TestWithExamInfoDTO dto = new TestWithExamInfoDTO();
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setTotalQuestion(totalQuestion);
            dto.setEasyQuestion(easyQuestion);
            dto.setMediumQuestion(mediumQuestion);
            dto.setHardQuestion(hardQuestion);
            dto.setDuration(duration);
            dto.setFormat(format);
            dto.setPoint(point);
            dto.setCourseId(courseId);
            dto.setIntro(intro);

            if (level != null) {
                ExamLevel examLevel = ExamLevel.valueOf(level.toUpperCase());
                dto.setLevel(examLevel);
            }
            if (status != null) {
                ExamStatus examStatus = ExamStatus.valueOf(status.toUpperCase());
                dto.setStatus(examStatus);
            }
            if (examType != null) {
                ExamType examType1 = ExamType.valueOf(examType.toUpperCase());
                dto.setExamType(examType1);
            }
            dto.setPrice(price != null ? new java.math.BigDecimal(price) : null);
            dto.setCost(cost != null ? new java.math.BigDecimal(cost) : null);
            dto.setType(type);
            TestWithExamInfoDTO newTest = testService.addExam(dto, image);
            if (newTest != null) {
                return new ResponseEntity<>(
                        new ApiResponse<>(HttpStatus.CREATED.value(), "Test created successfully", dto),
                        HttpStatus.CREATED
                );
            }
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Test creation failed", null),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping(value = "/update-exam/{testId}")
    public ResponseEntity<ApiResponse<TestWithExamInfoDTO>> updateTest(
            @PathVariable Integer testId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam Integer totalQuestion,
            @RequestParam(required = false) Integer easyQuestion,
            @RequestParam(required = false) Integer mediumQuestion,
            @RequestParam(required = false) Integer hardQuestion,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) Integer point,
            @RequestParam Integer courseId,

            // ExamInfo
            @RequestParam(required = false) String intro,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String cost,
            @RequestParam(required = false) String examType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestPart(value = "image", required = false) MultipartFile[] image
    ) {
        try {
            TestWithExamInfoDTO dto = new TestWithExamInfoDTO();
            dto.setTestId(testId);
            dto.setTitle(title);
            dto.setDescription(description);
            dto.setTotalQuestion(totalQuestion);
            dto.setEasyQuestion(easyQuestion);
            dto.setMediumQuestion(mediumQuestion);
            dto.setHardQuestion(hardQuestion);
            dto.setDuration(duration);
            dto.setFormat(format);
            dto.setPoint(point);
            dto.setCourseId(courseId);
            dto.setIntro(intro);

            if (level != null) dto.setLevel(ExamLevel.valueOf(level.toUpperCase()));
            if (status != null) dto.setStatus(ExamStatus.valueOf(status.toUpperCase()));
            if (examType != null) dto.setExamType(ExamType.valueOf(examType.toUpperCase()));

            dto.setPrice(price != null ? new java.math.BigDecimal(price) : null);
            dto.setCost(cost != null ? new java.math.BigDecimal(cost) : null);
            dto.setType(type);

            TestWithExamInfoDTO updated = testService.updateExam(dto, image);
            return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thành công", updated));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi: " + e.getMessage(), null));
        }
    }

    @PutMapping("/toggle-status/{testId}")
    public ResponseEntity<ApiResponse<?>> toggleExamStatus(@PathVariable Integer testId) {
        return ResponseEntity.ok(testService.toggleExamStatus(testId));
    }


    @PostMapping("/add-list")
    public ResponseEntity<String> addMultiTest(@RequestBody List<AdminTestAddDTO_V2> newTestDTO) {
        try {
            testService.AddMultiTest(newTestDTO);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/add-list-exam")
    public ResponseEntity<String> addMultiExam(@RequestBody List<AdminTestAddDTO_V2> newTestDTO) {
        try {
            testService.AddMultiExam(newTestDTO);
            return new ResponseEntity<>("Success", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error", HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/add-list-exam-preparation")
    public ResponseEntity<ApiResponse<Boolean>> addMultiExamPreparation(
            @RequestParam(value = "chapterId") Integer chapterId,
            @RequestParam(value = "estimate") Integer estimate,
            @RequestBody AdminTestAddDTO_V2 newTestDTO) {
        try {
            questionService.createExams(newTestDTO, newTestDTO.getType(), chapterId, estimate);
            return ResponseEntity.ok(new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Tạo bài thi thành công!",
                    true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Lỗi Server! Chi tiết: " + e.getMessage(),
                    null
            ));
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

    @PutMapping("/delete-exam/{id}")
    public ResponseEntity<?> deleteExamAdmin(@PathVariable int id) {
        try {
            testService.deleteExamAdmin(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Đã đánh dấu xóa bài thi và thông tin exam (nếu có)", "Deleted Test ID: " + id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "Không tìm thấy bài thi với ID: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PutMapping("/restore-exam/{id}")
    public ResponseEntity<?> restoreExamAdmin(@PathVariable int id) {
        try {
            testService.restoreExamAdmin(id);
            return ResponseEntity.ok(new ApiResponse<>(200, "Đã đánh dấu đã phục hồi bài thi và thông tin exam (nếu có)", "Restore Test ID: " + id));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "Không tìm thấy bài thi với ID: " + id, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PutMapping("/update-not-test/{testId}")
    public ResponseEntity<?> updateNotTest(@PathVariable int testId) {
        try {
            Test deletedTest = testService.updateNotTest(testId);
            return ResponseEntity.ok().body("Update successful!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + testId);
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

    @PutMapping("/update-to-lesson/{id}")
    public ApiResponse<Boolean> updateTestToLesson(@PathVariable int id, @RequestBody AdminUpdateTestToLesson updateDTO) {
        return testService.updateTestToLesson(id, updateDTO);
    }

    @RequestMapping(value = "/course/{courseId}/chapter/{chapterId}")
    public ResponseEntity<List<AdminTestDTORestoreList>> getTestsByCourseAndChapter(
            @PathVariable Integer courseId, @PathVariable Integer chapterId) {
        // Logic để lấy dữ liệu từ database
        List<AdminTestDTORestoreList> tests = testService.getTestsByCourseAndChapter(courseId, chapterId);
        return tests.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tests);
    }

    @GetMapping("/restore/list-all-tests")
    public Page<AdminTestDTORestoreList> getLessons(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer chapterId,
            @RequestParam(required = false) String testTitle,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (testTitle.equals("")) {
            testTitle = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }
        return testService.getTests(courseId, chapterId, testTitle, deletedDate, page, size);
    }

    @GetMapping("/deleted/list-all-exam")
    public ResponseEntity<ApiResponse<Page<TestWithExamInfoDTO>>> getExamDeleted(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
//            if (title.equals("")) {
//                title = null;
//            }
            Page<TestWithExamInfoDTO> result = testService.getExamDeleted(courseId, title, page, size);
            return ResponseEntity.ok(new ApiResponse<>(200, "Thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Lỗi: " + e.getMessage(), null));
        }
    }

    @PutMapping("/restore/{testId}")
    public ResponseEntity<Test> restoreTest(@PathVariable Integer testId) {
        AdminTestDTORestoreList testDTORestoreList = new AdminTestDTORestoreList();
        testDTORestoreList.setId(testId);
        Test restoreChapter = testService.updateRestoreTest(testDTORestoreList);
        return ResponseEntity.ok(restoreChapter);
    }

    @DeleteMapping("/delete/{testId}")
    public ResponseEntity<String> deleteTest(@PathVariable Integer testId) {
        AdminTestDTORestoreList testDTORestoreList = new AdminTestDTORestoreList();
        testDTORestoreList.setId(testId);
        testService.deleteRestoreTest(testDTORestoreList);
        return ResponseEntity.ok("Test permanently deleted.");
    }

    @PostMapping("/count/tests")
    public ApiResponse<Integer> getCountTest(
            @RequestParam(required = false) Integer chapterID,
            @RequestParam(required = false) Integer easyQuestion,
            @RequestParam(required = false) Integer mediumQuestion,
            @RequestParam(required = false) Integer hardQuestion,
            @RequestBody List<String> types) {

        return testService.getTotalTest(chapterID, easyQuestion, mediumQuestion, hardQuestion, types);
    }

    @GetMapping("/by-course")
    public ResponseEntity<Page<TestWithQuestionCountDTO>> getTestsWithQuestionCount(@RequestParam Integer courseId, Pageable pageable) {
        Page<TestWithQuestionCountDTO> tests = testService.getTestsWithQuestionCount(courseId, pageable);
        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{testId}/lesson-chapter-ids")
    public ResponseEntity<Map<String, Integer>> getLessonAndChapterId(@PathVariable int testId) {
        Map<String, Integer> ids = testService.getLessonAndChapterIdByTestId(testId);
        return ResponseEntity.ok(ids);
    }

}
