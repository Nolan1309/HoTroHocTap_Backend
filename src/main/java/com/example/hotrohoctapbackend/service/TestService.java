package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestGetDTO_Version2;
import com.example.hotrohoctapbackend.DTO.Admin.AdminTestUpdateDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminUpdateTestToLesson;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.ExamDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.ExamDetailDTOPublic;
import com.example.hotrohoctapbackend.DTO.AdminV3.Exam.TestWithExamInfoDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Test.TestDTO;
import com.example.hotrohoctapbackend.DTO.User.TestDTO_User;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.*;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.enums.DiscountStatus;
import com.example.hotrohoctapbackend.enums.ExamLevel;
import com.example.hotrohoctapbackend.enums.ExamStatus;
import com.example.hotrohoctapbackend.enums.ExamType;
import com.example.hotrohoctapbackend.exception.ApiResponse;
//import org.jetbrains.annotations.NotNull;
import com.google.firebase.database.annotations.NotNull;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
    private ExamInfoRepository examInfoRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ImageKitService imageKitService;
    @Autowired
    private Test_QuestionRepository testQuestionRepository;

    @Autowired
    private Course_DiscountRepository courseDiscountRepository;

    @Transactional
    public Test saveTest(Test test) {
        return testRepository.save(test);
    }

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
    public Test addTest(@NotNull AdminTestAddDTO_V2 newTestDTO) {
        // Kiểm tra xem đã có bài kiểm tra với course_id, chapter_id và is_summary chưa
        if (newTestDTO.getIsSummary()) {
            boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(newTestDTO.getCourseId(),
                    newTestDTO.getChapterId());


            if (exists) {
                throw new RuntimeException("A test with the same course_id, chapter_id, and is_summary already exists.");
            }
        }
        Map<String, String> typeMapping = new HashMap<>();
        typeMapping.put("Điền khuyết", "fill-in-the-blank");
        typeMapping.put("Tự luận", "essay");
        typeMapping.put("Checkbox", "checkbox");
        typeMapping.put("Trắc nghiệm", "multiple-choice");


        Test test = new Test();

        test.setTitle(newTestDTO.getTitle());
        test.setDescription(newTestDTO.getDescription());
        test.setTotalQuestion(newTestDTO.getTotalQuestion());
        test.setFormat(newTestDTO.getFormat());

        if (newTestDTO.getFormat().equals("exam")) {
            test.setAssigned(true);
        }

        if (newTestDTO.getLessonId() != null) {
            Lesson lesson = lessonRepository.findById(newTestDTO.getLessonId()).get();
            test.setLesson(lesson);
        }
        test.setSummary(newTestDTO.getIsSummary());

        if (newTestDTO.getIsSummary() && newTestDTO.getLessonId() == null) {
            test.setAssigned(true);
        } else if (!newTestDTO.getIsSummary() && newTestDTO.getLessonId() != null) {
            test.setAssigned(true);
            Lesson lesson = lessonRepository.findById(newTestDTO.getLessonId()).get();
            lesson.setIsTestExcluded("FULLTEST");
            lessonRepository.save(lesson);
            // Phải update lesson
        }

        test.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        test.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        test.setEasyQuestion(newTestDTO.getEasyQuestion());
        test.setMediumQuestion(newTestDTO.getMediumQuestion());
        test.setHardQuestion(newTestDTO.getHardQuestion());
        test.setDuration(newTestDTO.getDuration());


        List<String> types = newTestDTO.getType();
        String result = types.stream()
                .map(type -> typeMapping.getOrDefault(type, type)) // Ánh xạ tên hoặc giữ nguyên nếu không tìm thấy
                .collect(Collectors.joining(", "));

        test.setType(result);
        // Thêm các thông tin khác như chapter, course...
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

        // Lưu đối tượng test vào cơ sở dữ liệu
        return testRepository.save(test);
    }

    public TestWithExamInfoDTO addExam(TestWithExamInfoDTO dto, MultipartFile[] files) {
        try {
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Test test = new Test();
            test.setTitle(dto.getTitle());
            test.setDescription(dto.getDescription());
            test.setTotalQuestion(dto.getTotalQuestion());
            test.setEasyQuestion(dto.getEasyQuestion());
            test.setMediumQuestion(dto.getMediumQuestion());
            test.setHardQuestion(dto.getHardQuestion());
            test.setDuration(dto.getDuration());
            test.setFormat(dto.getFormat());
            test.setPoint(dto.getPoint());
            test.setCourse(course);


            test.setType(dto.getType());
            test.setCreatedAt(new Date());

            Test savedTest = testRepository.save(test);

            ExamInfo info = new ExamInfo();
            info.setTest(savedTest);
            info.setIntro(dto.getIntro());
            info.setLevel(dto.getLevel());
            info.setPrice(dto.getPrice());
            info.setCost(dto.getCost());
            info.setExamType(dto.getExamType());
            info.setStatus(dto.getStatus());

            // TODO: xử lý lưu file nếu cần
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        Result imageUrl = imageKitService.uploadFromBytes(file);
                        info.setImageUrl(imageUrl.getUrl());
                    }
                }
            }
            examInfoRepository.save(info);
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving test: " + e.getMessage());
        }
    }

    public TestWithExamInfoDTO updateExam(TestWithExamInfoDTO dto, MultipartFile[] files) {
        try {
            Test test = testRepository.findById(dto.getTestId())
                    .orElseThrow(() -> new RuntimeException("Test not found"));

            test.setTitle(dto.getTitle());
            test.setDescription(dto.getDescription());
            test.setTotalQuestion(dto.getTotalQuestion());
            test.setEasyQuestion(dto.getEasyQuestion());
            test.setMediumQuestion(dto.getMediumQuestion());
            test.setHardQuestion(dto.getHardQuestion());
            test.setDuration(dto.getDuration());
            test.setFormat(dto.getFormat());
            test.setPoint(dto.getPoint());
            test.setType(dto.getType());
            test.setUpdatedAt(new Date());

            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            test.setCourse(course);

            testRepository.save(test);

            ExamInfo info = examInfoRepository.findByTestId(dto.getTestId())
                    .orElseThrow(() -> new RuntimeException("ExamInfo not found"));

            info.setIntro(dto.getIntro());
            info.setLevel(dto.getLevel());
            info.setPrice(dto.getPrice());
            info.setCost(dto.getCost());
            info.setExamType(dto.getExamType());
            info.setStatus(dto.getStatus());
            info.setUpdatedAt(LocalDateTime.now());

//            List<ExamDiscount> activeDiscounts = examDiscountRepository.findActiveByExamInfoId(info.getId(), LocalDateTime.now());
//
//            if (!activeDiscounts.isEmpty()) {
//                Discount d = activeDiscounts.get(0).getDiscount(); // Ưu tiên 1 cái
//                BigDecimal discountAmount = info.getCost().multiply(BigDecimal.valueOf(d.getPercent())).divide(BigDecimal.valueOf(100));
//                BigDecimal finalPrice = info.getCost().subtract(discountAmount);
//
//                info.setPrice(finalPrice);
//            } else {
//                // Không có discount → cho phép nhập giá tay (nếu muốn)
//                info.setPrice(dto.getPrice());
//            }


            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        Result uploadResult = imageKitService.uploadFromBytes(file);
                        info.setImageUrl(uploadResult.getUrl());
                    }
                }
            }

            examInfoRepository.save(info);

            return dto;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    public ApiResponse<?> toggleExamStatus(Integer testId) {
        try {
            ExamInfo examInfo = examInfoRepository.findByTestId(testId)
                    .orElseThrow(() -> new RuntimeException("ExamInfo not found"));

            ExamStatus currentStatus = examInfo.getStatus();
            ExamStatus newStatus = currentStatus == ExamStatus.ACTIVE ? ExamStatus.INACTIVE : ExamStatus.ACTIVE;

            examInfo.setStatus(newStatus);
            examInfo.setUpdatedAt(LocalDateTime.now());

            examInfoRepository.save(examInfo);

            return new ApiResponse<>(200, "Cập nhật trạng thái thành công", newStatus.name());

        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(500, "Lỗi khi cập nhật trạng thái: " + e.getMessage(), null);
        }
    }


    public void AddMultiTest(List<AdminTestAddDTO_V2> newTestDTOList) {

        for (AdminTestAddDTO_V2 newTestDTO : newTestDTOList) {
            if (newTestDTO.getIsSummary()) {
                boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(newTestDTO.getCourseId(),
                        newTestDTO.getChapterId());
                if (exists) {
                    throw new IllegalArgumentException("A test with the same course_id, chapter_id, and is_summary already exists.");
                }
            }
        }
        for (AdminTestAddDTO_V2 newTestDTO : newTestDTOList) {
            // Kiểm tra xem đã có bài kiểm tra với course_id, chapter_id và is_summary chưa
            if (newTestDTO.getIsSummary()) {
                boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(newTestDTO.getCourseId(),
                        newTestDTO.getChapterId());

                if (exists) {
//                    throw new RuntimeException("A test with the same course_id, chapter_id, and is_summary already exists.");
                    throw new IllegalArgumentException("A test with the same course_id, chapter_id, and is_summary already exists.");
                }
            }
            Map<String, String> typeMapping = new HashMap<>();
            typeMapping.put("Điền khuyết", "fill-in-the-blank");
            typeMapping.put("Tự luận", "essay");
            typeMapping.put("Checkbox", "checkbox");
            typeMapping.put("Trắc nghiệm", "multiple-choice");


            Test test = new Test();

            test.setTitle(newTestDTO.getTitle());
            test.setFormat(newTestDTO.getFormat());
            test.setDescription(newTestDTO.getDescription());
            test.setTotalQuestion(newTestDTO.getTotalQuestion());

//            if (newTestDTO.getLessonId() != null){
//                Lesson lesson = lessonRepository.findById(newTestDTO.getLessonId()).get();
//                test.setLesson(lesson);
//
//                lesson.setIsTestExcluded("FULLTEST");
//                lessonRepository.saveAndFlush(lesson);
//            }
            test.setSummary(newTestDTO.getIsSummary());
//
//            if(newTestDTO.getIsSummary() && newTestDTO.getLessonId() == null)
//            {
//                test.setAssigned(true);
//            } else if ( !newTestDTO.getIsSummary() && newTestDTO.getLessonId() != null) {
//                test.setAssigned(true);
//            }


            test.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

            test.setEasyQuestion(newTestDTO.getEasyQuestion());
            test.setMediumQuestion(newTestDTO.getMediumQuestion());
            test.setHardQuestion(newTestDTO.getHardQuestion());
            test.setDuration(newTestDTO.getDuration());
            List<String> types = newTestDTO.getType();
            String result = types.stream()
                    .map(type -> typeMapping.getOrDefault(type, type)) // Ánh xạ tên hoặc giữ nguyên nếu không tìm thấy
                    .collect(Collectors.joining(", "));

            test.setType(result);

            // Thêm các thông tin khác như chapter, course...
            if (newTestDTO.getChapterId() != null) {
                Chapter chapter = chapterRepository.findById(newTestDTO.getChapterId())
                        .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
                test.setChapter(chapter);
                if (newTestDTO.getLessonId() != null) {
                    test.setAssigned(true);
                    Lesson lesson = lessonRepository.findById(newTestDTO.getLessonId()).get();
                    lesson.setIsTestExcluded("FULLTEST");
                    lessonRepository.saveAndFlush(lesson);
                } else if (newTestDTO.getLessonId() == null && newTestDTO.getIsSummary()) {
                    test.setAssigned(true);
                } else {
                    test.setAssigned(false);
                }
            }


            if (newTestDTO.getCourseId() != null) {
                Course course = courseRepository.findById(newTestDTO.getCourseId())
                        .orElseThrow(() -> new IllegalArgumentException("Course not found"));
                test.setCourse(course);
            }
            testRepository.save(test);
        }
    }


    public void AddMultiExam(List<AdminTestAddDTO_V2> newTestDTOList) {

        for (AdminTestAddDTO_V2 newTestDTO : newTestDTOList) {
            if (newTestDTO.getIsSummary()) {
                boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(newTestDTO.getCourseId(),
                        newTestDTO.getChapterId());
                if (exists) {
                    throw new IllegalArgumentException("A test with the same course_id, chapter_id, and is_summary already exists.");
                }
            }
        }
        for (AdminTestAddDTO_V2 newTestDTO : newTestDTOList) {
            // Kiểm tra xem đã có bài kiểm tra với course_id, chapter_id và is_summary chưa
            if (newTestDTO.getIsSummary()) {
                boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(newTestDTO.getCourseId(),
                        newTestDTO.getChapterId());
                if (exists) {
                    throw new IllegalArgumentException("A test with the same course_id, chapter_id, and is_summary already exists.");
                }
            }
            Map<String, String> typeMapping = new HashMap<>();
            typeMapping.put("Điền khuyết", "fill-in-the-blank");
            typeMapping.put("Tự luận", "essay");
            typeMapping.put("Checkbox", "checkbox");
            typeMapping.put("Trắc nghiệm", "multiple-choice");


            Test test = new Test();

            test.setTitle(newTestDTO.getTitle());
            test.setFormat(newTestDTO.getFormat());
            test.setDescription(newTestDTO.getDescription());
            test.setTotalQuestion(newTestDTO.getTotalQuestion());
            test.setSummary(newTestDTO.getIsSummary());
            test.setCreatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            test.setEasyQuestion(newTestDTO.getEasyQuestion());
            test.setMediumQuestion(newTestDTO.getMediumQuestion());
            test.setHardQuestion(newTestDTO.getHardQuestion());
            test.setDuration(newTestDTO.getDuration());
            List<String> types = newTestDTO.getType();
            String result = types.stream()
                    .map(type -> typeMapping.getOrDefault(type, type)) // Ánh xạ tên hoặc giữ nguyên nếu không tìm thấy
                    .collect(Collectors.joining(", "));

            test.setType(result);

            // Thêm các thông tin khác như chapter, course...
            if (newTestDTO.getChapterId() != null) {
                Chapter chapter = chapterRepository.findById(newTestDTO.getChapterId())
                        .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
                test.setChapter(chapter);
                test.setAssigned(true);
            }

            if (newTestDTO.getCourseId() != null) {
                Course course = courseRepository.findById(newTestDTO.getCourseId())
                        .orElseThrow(() -> new IllegalArgumentException("Course not found"));
                test.setCourse(course);
            }
            testRepository.save(test);
        }
    }


    @Transactional
    public Boolean updateTest(int id, AdminTestGetListDTO_V2 updateDTO) {
        // Lấy Test từ database
        Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
        List<Test_Question> listQuestion = testQuestionRepository.findTestAnswersByTestId(id);

        if (!listQuestion.isEmpty() && listQuestion.size() > updateDTO.getTotalQuestion()) {
            throw new RuntimeException("Tổng số câu hỏi không thể nhỏ hơn số lượng câu hỏi hiện có trong bài kiểm tra.");
        }
        int total = updateDTO.getEasyQuestion() + updateDTO.getMediumQuestion() + updateDTO.getHardQuestion();
        if (total < updateDTO.getTotalQuestion() || total > updateDTO.getTotalQuestion()) {
            throw new RuntimeException("Tỉ lệ số câu phải bằng tổng số câu hỏi trong bài kiểm tra.");
        }

        test.setUpdatedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        // Cập nhật các trường
        if (updateDTO.getTitle() != null) {
            test.setTitle(updateDTO.getTitle());
        }

        if (updateDTO.getDescription() != null) {
            test.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getLessonId() != null) {
            test.setLesson(lessonRepository.findById(updateDTO.getLessonId()).get());
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

        test.setTotalQuestion(updateDTO.getTotalQuestion());

        if (updateDTO.getSummary()) {
            boolean exists = testRepository.existsByCourseIdAndChapterIdAndIsSummary(updateDTO.getCourseId(),
                    updateDTO.getChapterId());
            if (exists) {
                throw new IllegalArgumentException("A test with the same course_id, chapter_id, and is_summary already exists.");
            }

        }
        test.setSummary(updateDTO.getSummary());
        test.setEasyQuestion(updateDTO.getEasyQuestion());
        test.setMediumQuestion(updateDTO.getMediumQuestion());
        test.setHardQuestion(updateDTO.getHardQuestion());
        test.setDuration(updateDTO.getDuration());
        test.setPoint(updateDTO.getPoint());

        if (updateDTO.getType() != null) {
            test.setType(String.join(",", updateDTO.getType())); // Convert List<String> thành String
        }
        // Lưu lại

        Test item = testRepository.save(test);
        if (item != null) {
            return true;
        }
        return false;
    }

    @Transactional
    public ApiResponse<Boolean> updateTestToLesson(int id, AdminUpdateTestToLesson updateDTO) {
        try {
            Test test = testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found"));
            Lesson lesson = lessonRepository.findById(updateDTO.getLessonId()).orElseThrow(() -> new RuntimeException("Lesson not found"));

            test.setLesson(lesson);
            test.setAssigned(true);
            test.setSummary(false);

            // Cập nhật trạng thái của lesson
            lesson.setIsTestExcluded("FULLTEST");
            lessonRepository.saveAndFlush(lesson);

            // Cập nhật test
            testRepository.saveAndFlush(test);

            return new ApiResponse<>(200, "Success", true);  // Trả về true nếu cập nhật thành công
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error: " + e.getMessage(), false);  // Trả về false nếu có lỗi
        }
    }


    @Transactional
    public AdminTestGetListDTO_V2 getTestByIdAdmin(int id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        List<String> questionTypes = (test.getType() != null)
                ? Arrays.asList(((String) test.getType()).split(",\\s*")) // Tách chuỗi nếu không null
                : Collections.emptyList();

        // Khởi tạo DTO và thiết lập giá trị bằng setter
        AdminTestGetListDTO_V2 responseDTO = new AdminTestGetListDTO_V2();
        responseDTO.setId(test.getId());
        responseDTO.setTitle(test.getTitle());
        responseDTO.setDescription(test.getDescription());
        responseDTO.setEasyQuestion(test.getEasyQuestion());
        responseDTO.setMediumQuestion(test.getMediumQuestion());
        responseDTO.setHardQuestion(test.getHardQuestion());

        // Kiểm tra null trước khi set giá trị
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
        responseDTO.setSummary(test.isSummary());
        responseDTO.setType(questionTypes);  // Gán vào DTO
        responseDTO.setDuration(test.getDuration());
        return responseDTO;
    }

    public Test addTestToLessonAdmin(Test test) {
        return testRepository.saveAndFlush(test);
    }


    public Page<AdminTestGetDTO_Version2> getAllTestSummariesAdmin(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable từ page và size
        Page<Object[]> testSummaries = testRepository.findAllTestSummaries(pageable); // Lấy dữ liệu phân trang

        // Chuyển kết quả từ Page<Object[]> thành Page<AdminTestGetDTO>
        return testSummaries.map(result -> new AdminTestGetDTO_Version2(
                (Integer) result[0],                // id
                (String) result[1],                 // title
                (Integer) result[2],                // totalQuestion
                (Date) result[3],                   // createdAt
                (Boolean) result[4],                // isDeleted
                (Boolean) result[5],
                (Integer) result[6],
                (Integer) result[7]
        ));
    }

    public Page<AdminTestGetListDTO_V2> getAllTestSummariesAdmin_V2(Integer courseId, String title, Pageable pageable) {
        // Lấy dữ liệu phân trang
        Page<Object[]> testSummaries = testRepository.findByCourseIdAndTitleWithPagination(courseId, title, pageable);

        // Tạo danh sách mới để lưu kết quả
        List<AdminTestGetListDTO_V2> results = new ArrayList<>();

        // Sử dụng vòng lặp for để chuyển đổi từng phần tử
        for (Object[] result : testSummaries.getContent()) {
            List<String> types = (result[6] != null)
                    ? Arrays.asList(((String) result[6]).split(",\\s*")) // Tách chuỗi nếu không null
                    : Collections.emptyList();
            AdminTestGetListDTO_V2 dto = new AdminTestGetListDTO_V2(
                    (Integer) result[0],  // id
                    (String) result[1],   // title
                    (Integer) result[2],  // totalQuestion
                    (Integer) result[3],  // field 4
                    (Integer) result[4],  // field 5
                    (Integer) result[5],  // field 6
                    types,   // field 7
                    (Date) result[7],     // createdAt
                    (Boolean) result[8],  // isDeleted
                    (Boolean) result[9],  // field 9
                    (Integer) result[10], // field 10
                    (Integer) result[11], // field 11
                    (Integer) result[12], // field 12
                    (String) result[13],
                    (Boolean) result[14],
                    (Integer) result[15],
                    (String) result[16],
                    (Integer) result[17]

            );
            results.add(dto);
        }

        // Trả về đối tượng Page từ danh sách đã chuyển đổi
        return new PageImpl<>(results, pageable, testSummaries.getTotalElements());
    }

    public Page<AdminTestGetListDTO_V2> getAllTestSummariesAdmin_V2_Exam(Integer courseId, String title, Pageable pageable) {
        // Lấy dữ liệu phân trang
        Page<Object[]> testSummaries = testRepository.findByCourseIdAndTitleWithPaginationExam(courseId, title, pageable);

        // Tạo danh sách mới để lưu kết quả
        List<AdminTestGetListDTO_V2> results = new ArrayList<>();

        // Sử dụng vòng lặp for để chuyển đổi từng phần tử
        for (Object[] result : testSummaries.getContent()) {
            List<String> types = (result[6] != null)
                    ? Arrays.asList(((String) result[6]).split(",\\s*"))
                    : Collections.emptyList();
            AdminTestGetListDTO_V2 dto = new AdminTestGetListDTO_V2(
                    (Integer) result[0],  // id
                    (String) result[1],   // title
                    (Integer) result[2],  // totalQuestion
                    (Integer) result[3],  // field 4
                    (Integer) result[4],  // field 5
                    (Integer) result[5],  // field 6
                    types,   // field 7
                    (Date) result[7],     // createdAt
                    (Boolean) result[8],  // isDeleted
                    (Boolean) result[9],  // field 9
                    (Integer) result[10], // field 10
                    (Integer) result[11], // field 11
                    (Integer) result[12], // field 12
                    (String) result[13],
                    (Boolean) result[14],
                    (Integer) result[15],
                    (String) result[16],
                    (Integer) result[17]

            );
            results.add(dto);
        }

        // Trả về đối tượng Page từ danh sách đã chuyển đổi
        return new PageImpl<>(results, pageable, testSummaries.getTotalElements());
    }

    public Page<TestWithExamInfoDTO> getFilteredTests(String title, Integer courseId, Pageable pageable) {
        Page<Test> tests = testRepository.findFiltered(title, courseId, pageable);

        return tests.map(test -> {
            TestWithExamInfoDTO dto = new TestWithExamInfoDTO();
            dto.setTestId(test.getId());
            dto.setTitle(test.getTitle());
            dto.setDescription(test.getDescription());
            dto.setTotalQuestion(test.getTotalQuestion());
            dto.setEasyQuestion(test.getEasyQuestion());
            dto.setMediumQuestion(test.getMediumQuestion());
            dto.setHardQuestion(test.getHardQuestion());
            dto.setType(test.getType());
            dto.setFormat(test.getFormat());
            dto.setDuration(test.getDuration());
            dto.setPoint(test.getPoint());
            dto.setCreatedAt(test.getCreatedAt());
            dto.setUpdatedAt(test.getUpdatedAt());
            if ("exam".equalsIgnoreCase(test.getFormat())) {
                dto.setItemCount(test.getTestEnrollments().size());
            }

            if (test.getCourse() != null) {
                dto.setCourseId(test.getCourse().getId());
                dto.setCourseTitle(test.getCourse().getTitle());
            }

            if ("exam".equalsIgnoreCase(test.getFormat())) {


                examInfoRepository.findByTestId(test.getId()).ifPresent(info -> {
                    dto.setIntro(info.getIntro());
                    dto.setImageUrl(info.getImageUrl());
                    dto.setLevel(info.getLevel());
                    dto.setPrice(info.getPrice());
                    dto.setCost(info.getCost());
                    dto.setExamType(info.getExamType());
                    dto.setStatus(info.getStatus());
                });
            }
            return dto;
        });
    }

    public List<TestWithExamInfoDTO> getFilteredExamList(Integer courseId) {
        List<Test> tests = testRepository.findFilteredExaList(courseId);

        return tests.stream().map(test -> {
            TestWithExamInfoDTO dto = new TestWithExamInfoDTO();
            dto.setTestId(test.getId());
            dto.setTitle(test.getTitle());
            dto.setDescription(test.getDescription());
            dto.setTotalQuestion(test.getTotalQuestion());
            dto.setEasyQuestion(test.getEasyQuestion());
            dto.setMediumQuestion(test.getMediumQuestion());
            dto.setHardQuestion(test.getHardQuestion());
            dto.setType(test.getType());
            dto.setFormat(test.getFormat());
            dto.setDuration(test.getDuration());
            dto.setPoint(test.getPoint());
            dto.setCreatedAt(test.getCreatedAt());
            dto.setUpdatedAt(test.getUpdatedAt());

            if ("exam".equalsIgnoreCase(test.getFormat())) {
                dto.setItemCount(test.getTestEnrollments() != null ? test.getTestEnrollments().size() : 0);
            }

            if (test.getCourse() != null) {
                dto.setCourseId(test.getCourse().getId());
                dto.setCourseTitle(test.getCourse().getTitle());
            }

            if ("exam".equalsIgnoreCase(test.getFormat())) {
                examInfoRepository.findByTestId(test.getId()).ifPresent(info -> {
                    dto.setIntro(info.getIntro());
                    dto.setImageUrl(info.getImageUrl());
                    dto.setLevel(info.getLevel());
                    dto.setPrice(info.getPrice());
                    dto.setCost(info.getCost());
                    dto.setExamType(info.getExamType());
                    dto.setStatus(info.getStatus());
                });
            }

            return dto;
        }).collect(Collectors.toList());
    }

    //    }
    public List<AdminTestGetDTO> getAllTestSummariesAdminList() {
        List<Object[]> testSummaries = testRepository.findAllTestSummariesList(); // Lấy dữ liệu phân trang
        // You can add a constructor if needed
        List<AdminTestGetDTO> adminTestGetDTOList = new ArrayList<>();
        for (Object[] item : testSummaries) {
            AdminTestGetDTO it2 = new AdminTestGetDTO((Integer) item[0], (String) item[1], (Integer) item[2], (Date) item[3], (Boolean) item[4]);
            adminTestGetDTOList.add(it2);
        }
        return adminTestGetDTOList;
    }

    public void deleteExamAdmin(int id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Test not found"));
        test.setDeleted(true);
        test.setDeletedDate(LocalDateTime.now());
        testRepository.save(test);
        if ("exam".equalsIgnoreCase(test.getType())) {
            examInfoRepository.findByTestId(id).ifPresent(examInfo -> {
                examInfo.setDeleted(true);
                examInfo.setDeletedAt(LocalDateTime.now());
                examInfoRepository.save(examInfo);
            });
        }

    }

    public void restoreExamAdmin(int id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Test not found"));
        test.setDeleted(false);
        testRepository.save(test);
        if ("exam".equalsIgnoreCase(test.getType())) {
            examInfoRepository.findByTestId(id).ifPresent(examInfo -> {
                examInfo.setDeleted(false);
                examInfoRepository.save(examInfo);
            });
        }
    }

    public Test deleteTestAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Test> testOptional = testRepository.findById(testID);

        if (testOptional.isPresent()) {
            Test test = testOptional.get();

            test.setDeleted(true);
            test.setAssigned(false);
            test.setDeletedDate(LocalDateTime.now());

            if (testOptional.get().getLesson() != null) {
                Lesson lesson = lessonRepository.findById(testOptional.get().getLesson().getId()).get();
                lesson.setIsTestExcluded("EMPTYTEST");
                test.setLesson(null);
                lessonRepository.save(lesson);
            }
            // Lưu thay đổi
            return testRepository.save(test);
        } else {
            throw new RuntimeException("Test not found with id: " + testID);
        }
    }

    public Test updateNotTest(int testID) {
        // Tìm tài khoản theo ID
        Optional<Test> accountOpt = testRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Test test = accountOpt.get();
            if (accountOpt.get().getLesson() != null) {
                Lesson lesson = lessonRepository.findById(accountOpt.get().getLesson().getId()).get();
                lesson.setIsTestExcluded("EMPTYTEST");
                lessonRepository.save(lesson);
            }
            test.setAssigned(false);
            test.setLesson(null);
            test.setSummary(false);
            // Lưu thay đổi
            return testRepository.save(test);
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

    public List<AdminTestDTORestoreList> getTestsByCourseAndChapter(Integer courseId, Integer chapterId) {

        List<Object[]> listObject = testRepository.findTestsByCourseAndChapter(courseId, chapterId);

        List<AdminTestDTORestoreList> adminTestDTORestoreLists = new ArrayList<>();
        for (Object[] result : listObject) {
            AdminTestDTORestoreList dto = new AdminTestDTORestoreList();

            dto.setId((Integer) result[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[1]);
            dto.setCreatedAt(createAt);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[2]);
            dto.setDeletedDate(deleteAt);

            dto.setDescription((String) result[3]);
            dto.setIsDeleted((Boolean) result[4]);
            dto.setIsSummary((Boolean) result[5]);
            dto.setTitle((String) result[6]);
            dto.setTotalQuestion((Integer) result[7]);

            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[8]);
            dto.setUpdatedAt(updateAt);

            dto.setChapterId((Integer) result[9]);
            dto.setCourseId((Integer) result[10]);

            dto.setLessonId((Integer) result[11]);
            dto.setDuration((Integer) result[12]);
            dto.setEasyQuestion((Integer) result[13]);
            dto.setHardQuestion((Integer) result[14]);
            dto.setMediumQuestion((Integer) result[15]);
            dto.setIsAssigned((Boolean) result[16]);
            dto.setType((String) result[17]);

            adminTestDTORestoreLists.add(dto);
        }
        return adminTestDTORestoreLists;
    }

    public AdminTestCheckInfoCourse getChapterAndLessonSummary(int courseId) {
        AdminTestCheckInfoCourse adminTestCheckInfoCourse = new AdminTestCheckInfoCourse();
        int totalChapters = testRepository.countTotalChapters(courseId);
        int totalAssignedChapters = testRepository.countAssignedChapters(courseId);

        int totalLessons = testRepository.countTotalLessons(courseId);
        int totalAssignedLessons = testRepository.countAssignedLessons(courseId);
        int countAssignedTest = getAssignedTestsCount(courseId);
        int countUnAssignedTest = getUnassignedTestsCount(courseId);
        int countTestsByCourse = getTestsCountByCourse(courseId);
        adminTestCheckInfoCourse.setTotalChapters(totalChapters);
        adminTestCheckInfoCourse.setTotalLessons(totalLessons);
        adminTestCheckInfoCourse.setTotalAssignedChapter(totalAssignedChapters);
        adminTestCheckInfoCourse.setTotalAssignedLessons(totalAssignedLessons);
        adminTestCheckInfoCourse.setCountAssignedTests(countAssignedTest);
        adminTestCheckInfoCourse.setCountUnassignedTests(countUnAssignedTest);
        adminTestCheckInfoCourse.setCountTestByCourse(countTestsByCourse);
        return adminTestCheckInfoCourse;
    }

    public int getAssignedTestsCount(int courseId) {
        return testRepository.countAssignedTests(courseId);
    }

    public int getUnassignedTestsCount(int courseId) {
        return testRepository.countUnassignedTests(courseId);
    }

    public int getTestsCountByCourse(int courseId) {
        return testRepository.countTestsByCourse(courseId);
    }

    private LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) timestampObj;
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Page<AdminTestDTORestoreList> getTests(Integer courseId, Integer chapterId, String testTitle, String deletedDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> resultPage = testRepository.findTestsRestore(courseId, chapterId, testTitle, deletedDate, pageable);
        List<AdminTestDTORestoreList> adminTestDTORestoreLists = new ArrayList<>();
        for (Object[] result : resultPage) {
            AdminTestDTORestoreList dto = new AdminTestDTORestoreList();
            dto.setId((Integer) result[0]);
            LocalDateTime createAt = convertTimestampToLocalDateTime(result[1]);
            dto.setCreatedAt(createAt);
            LocalDateTime deleteAt = convertTimestampToLocalDateTime(result[2]);
            dto.setDeletedDate(deleteAt);

            dto.setDescription((String) result[3]);
            dto.setIsDeleted((Boolean) result[4]);
            dto.setIsSummary((Boolean) result[5]);
            dto.setTitle((String) result[6]);
            dto.setTotalQuestion((Integer) result[7]);

            LocalDateTime updateAt = convertTimestampToLocalDateTime(result[8]);
            dto.setUpdatedAt(updateAt);

            dto.setChapterId((Integer) result[9]);
            dto.setCourseId((Integer) result[10]);

            dto.setLessonId((Integer) result[11]);

            dto.setEasyQuestion((Integer) result[12]);
            dto.setHardQuestion((Integer) result[13]);
            dto.setMediumQuestion((Integer) result[14]);

            dto.setType((String) result[15]);
            dto.setIsAssigned((Boolean) result[16]);
            adminTestDTORestoreLists.add(dto);
        }
        return new PageImpl<>(adminTestDTORestoreLists, pageable, resultPage.getTotalElements());
    }

    public Page<TestWithExamInfoDTO> getExamDeleted(Integer courseId, String testTitle, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Test> resultPage = testRepository.findExamDeleted(courseId, testTitle, pageable);
        return resultPage.map(test -> {
            TestWithExamInfoDTO dto = new TestWithExamInfoDTO();
            dto.setTestId(test.getId());
            dto.setTitle(test.getTitle());
            dto.setDescription(test.getDescription());
            dto.setTotalQuestion(test.getTotalQuestion());
            dto.setEasyQuestion(test.getEasyQuestion());
            dto.setMediumQuestion(test.getMediumQuestion());
            dto.setHardQuestion(test.getHardQuestion());
            dto.setType(test.getType());
            dto.setFormat(test.getFormat());
            dto.setDuration(test.getDuration());
            dto.setPoint(test.getPoint());
            dto.setCreatedAt(test.getCreatedAt());
            dto.setUpdatedAt(test.getUpdatedAt());
            if ("exam".equalsIgnoreCase(test.getFormat())) {
                dto.setItemCount(test.getTestEnrollments().size());
            }

            if (test.getCourse() != null) {
                dto.setCourseId(test.getCourse().getId());
                dto.setCourseTitle(test.getCourse().getTitle());
            }

            if ("exam".equalsIgnoreCase(test.getFormat())) {


                examInfoRepository.findByTestId(test.getId()).ifPresent(info -> {
                    dto.setIntro(info.getIntro());
                    dto.setImageUrl(info.getImageUrl());
                    dto.setLevel(info.getLevel());
                    dto.setPrice(info.getPrice());
                    dto.setCost(info.getCost());
                    dto.setExamType(info.getExamType());
                    dto.setStatus(info.getStatus());
                });
            }
            return dto;
        });
    }

    public Test updateRestoreTest(AdminTestDTORestoreList adminTestDTORestoreList) {
        Optional<Test> lessonOptional = testRepository.findById(adminTestDTORestoreList.getId());
        if (lessonOptional.isEmpty()) {
            throw new RuntimeException("Test not found with id: " + adminTestDTORestoreList.getId());
        } else {
            Test test = lessonOptional.get();
            test.setDeleted(false);
            return testRepository.save(test);
        }
    }

    public void deleteRestoreTest(AdminTestDTORestoreList adminTestDTORestoreList) {
        Optional<Test> lessonOptional = testRepository.findById(adminTestDTORestoreList.getId());
        if (lessonOptional.isEmpty()) {
            throw new RuntimeException("Test not found with id: " + adminTestDTORestoreList.getId());
        } else {
            testRepository.delete(lessonOptional.get());
        }
    }


    public ApiResponse<Integer> getTotalTest(Integer chapterID, Integer easyQuestion, Integer mediumQuestion, Integer hardQuestion, List<String> types) {
        // Lấy danh sách câu hỏi theo loại và mức độ
        List<QuestionCountDTO> questionCountDTOs = questionService.getQuestionsCountByLevel(chapterID);

        // Lọc câu hỏi theo các loại trong types
        long totalEasyQuestions = 0;
        long totalMediumQuestions = 0;
        long totalHardQuestions = 0;

        Map<String, String> typeMapping = new HashMap<>();
        typeMapping.put("fill-in-the-blank", "Điền khuyết");
        typeMapping.put("essay", "Tự luận");
        typeMapping.put("checkbox", "Checkbox");
        typeMapping.put("multiple-choice", "Trắc nghiệm");

        for (QuestionCountDTO item : questionCountDTOs) {

            String questionType = typeMapping.get(item.getQuestionType());

            if (questionType != null && types.contains(questionType)) {
                totalEasyQuestions += item.getEasyQuestions();
                totalMediumQuestions += item.getMediumQuestions();
                totalHardQuestions += item.getHardQuestions();
            }
        }

        // Xử lý các trường hợp:
        int possibleTests = Integer.MAX_VALUE;  // Khởi tạo với giá trị tối đa để so sánh

        // Kiểm tra các trường hợp cho câu hỏi dễ
        if (easyQuestion > 0 && totalEasyQuestions > 0) {
            possibleTests = Math.min(possibleTests, (int) Math.floor(totalEasyQuestions / (double) easyQuestion));
        }
//        else if (easyQuestion == 0 && totalEasyQuestions > 0) {
//            possibleTests = 0;  // Nếu easyQuestion = 0 và có câu hỏi dễ, không thể tạo bài kiểm tra
//        }

        // Kiểm tra các trường hợp cho câu hỏi trung bình
        if (mediumQuestion > 0 && totalMediumQuestions > 0) {
            possibleTests = Math.min(possibleTests, (int) Math.floor(totalMediumQuestions / (double) mediumQuestion));
        }
//        else if (mediumQuestion == 0 && totalMediumQuestions > 0) {
//            possibleTests = 0;  // Nếu mediumQuestion = 0 và có câu hỏi trung bình, không thể tạo bài kiểm tra
//        }

        // Kiểm tra các trường hợp cho câu hỏi khó
        if (hardQuestion > 0 && totalHardQuestions > 0) {
            possibleTests = Math.min(possibleTests, (int) Math.floor(totalHardQuestions / (double) hardQuestion));
        }
//        else if (hardQuestion == 0 && totalHardQuestions > 0) {
//            possibleTests = 0;  // Nếu hardQuestion = 0 và có câu hỏi khó, không thể tạo bài kiểm tra
//        }

        // Trả về số bài kiểm tra có thể có
        ApiResponse<Integer> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Thành công!",
                possibleTests == Integer.MAX_VALUE ? 0 : possibleTests
        );
        return response;

    }

    public Page<TestWithQuestionCountDTO> getTestsWithQuestionCount(Integer courseId, Pageable pageable) {
        Page<Object[]> results = testRepository.findTestsWithQuestionCountByCourseId(courseId, pageable);

        // Ánh xạ kết quả từ Object[] thành DTO
        return results.map(result -> {
            Integer testId = (Integer) result[0];
            String title = (String) result[1];
            String description = (String) result[2];
            Integer duration = (Integer) result[3];
            String testType = (String) result[4];
            Long totalQuestions = (Long) result[5];

            return new TestWithQuestionCountDTO(testId, title, description, duration, testType, totalQuestions);
        });
    }

    public Map<String, Integer> getLessonAndChapterIdByTestId(int testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        Lesson lesson = test.getLesson();
        Chapter chapter = test.getChapter();

        Map<String, Integer> ids = new HashMap<>();
        ids.put("lessonId", lesson.getId());
        ids.put("chapterId", chapter.getId());
        return ids;
    }

    public List<TestDTO> getTestsByCourseId(int courseId) {
        List<Test> tests = testRepository.findByCourseIdAndIsDeletedFalse(courseId);
//        List<String> types = newTestDTO.getType();
//        String result = types.stream()
//                .map(type -> typeMapping.getOrDefault(type, type)) // Ánh xạ tên hoặc giữ nguyên nếu không tìm thấy
//                .collect(Collectors.joining(", "));
        return tests.stream()
                .map(test -> new TestDTO(String.valueOf(test.getId()), test.getTitle(), test.getType(), test.getLesson() != null ? test.getLesson().getId() : null,
                        test.getChapter().getId(), test.getCourse().getId(), test.getDescription(), test.isSummary(),
                        test.getTotalQuestion(), test.getEasyQuestion(), test.getMediumQuestion(), test.getHardQuestion(),
                        test.getCreatedAt().toString(), test.getUpdatedAt() != null ? test.getUpdatedAt().toString() : "",
                        test.getDeletedDate() != null ? test.getDeletedDate().toString() : "", test.isDeleted(),
                        test.getDuration(), test.getFormat(), test.isAssigned(), test.getPoint()))
                .collect(Collectors.toList());
    }

    public List<TestDTO> getTestsByCourseIdAndChapterId(Integer chapterId, Integer courseId) {
        List<Test> tests = testRepository.findTestsByChapterAndCourse(chapterId, courseId);
        return tests.stream()
                .map(test -> new TestDTO(String.valueOf(test.getId()), test.getTitle(), test.getType(), test.getLesson() != null ? test.getLesson().getId() : null,
                        test.getChapter().getId(), test.getCourse().getId(), test.getDescription(), test.isSummary(),
                        test.getTotalQuestion(), test.getEasyQuestion(), test.getMediumQuestion(), test.getHardQuestion(),
                        test.getCreatedAt().toString(), test.getUpdatedAt() != null ? test.getUpdatedAt().toString() : "",
                        test.getDeletedDate() != null ? test.getDeletedDate().toString() : "", test.isDeleted(),
                        test.getDuration(), test.getFormat(), test.isAssigned(), test.getPoint()))
                .collect(Collectors.toList());
    }

    public Page<ExamDTOPublic> getTestsByCourseAndTitle(Integer courseId, String title, Integer accountId, int page, int size) {
        Page<Test> tests = testRepository.findByCourseAndTitleContaining(courseId, title, PageRequest.of(page, size));

        return tests.map(test -> {
            ExamDTOPublic examDTO = new ExamDTOPublic();
            examDTO.setTestId(test.getId());
            examDTO.setTitle(test.getTitle());
            examDTO.setDescription(test.getDescription());
            examDTO.setCourseId(test.getCourse().getId());
            examDTO.setCourseTitle(test.getCourse().getTitle());
            examDTO.setTotalQuestion(test.getTotalQuestion());
            examDTO.setDuration(test.getDuration());
            ExamInfo examInfo = examInfoRepository.findByTestId(test.getId()).orElse(null);
            if (examInfo != null) {
                examDTO.setImageUrl(examInfo.getImageUrl());
                examDTO.setLevel(examInfo.getLevel());
                examDTO.setStatus(examInfo.getStatus());
                examDTO.setExamType(examInfo.getExamType());
                examDTO.setCreatedAt(examInfo.getCreatedAt());
                examDTO.setUpdatedAt(examInfo.getUpdatedAt());
                examDTO.setPrice(examInfo.getPrice());
                examDTO.setCost(examInfo.getCost());
            }

            Double averageRating = testRepository.findAverageRatingByTestId(test.getId());
            examDTO.setRating(averageRating != null ? averageRating : 0.0);


            int itemCountReview = test.getReviewList() != null ? test.getReviewList().size() : 0;
            examDTO.setItemCountReview(itemCountReview);
            int itemCountPrice = test.getTestEnrollments() != null ? test.getTestEnrollments().size() : 0;
            examDTO.setItemCountPrice(itemCountPrice);

            if (accountId != null) {
                boolean isPurchased = test.getTestEnrollments().stream()
                        .anyMatch(te -> te.getAccount().getId() == accountId);
                examDTO.setPurchased(isPurchased);
            } else {
                examDTO.setPurchased(false);
            }

            Optional<Course_Discount> courseDiscountOpt = courseDiscountRepository.findByTestId(test.getId());
            if (courseDiscountOpt.isPresent() && courseDiscountOpt.get().getDiscount() != null && courseDiscountOpt.get().getDiscount().getStatus() == DiscountStatus.ACTIVE && examInfo.getExamType() == ExamType.FEE) {
                int intValue = courseDiscountOpt.get().getDiscount().getDiscountValue().intValue();
                examDTO.setPercentDiscount(intValue);
            } else {
                examDTO.setPercentDiscount(0);
            }


            return examDTO;
        });
    }

    public ExamDetailDTOPublic getTestDetails(Integer testId, Integer accountId) {

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new NoSuchElementException("Test not found for ID: " + testId));

        ExamDetailDTOPublic examDTO = new ExamDetailDTOPublic();
        examDTO.setTestId(test.getId());
        examDTO.setTitle(test.getTitle());
        examDTO.setDescription(test.getDescription());
        examDTO.setCourseId(test.getCourse().getId());
        examDTO.setCourseTitle(test.getCourse().getTitle());
        examDTO.setTotalQuestion(test.getTotalQuestion());
        examDTO.setDuration(test.getDuration());

        ExamInfo examInfo = examInfoRepository.findByTestId(test.getId()).orElse(null);
        if (examInfo != null) {
            examDTO.setImageUrl(examInfo.getImageUrl());
            examDTO.setLevel(examInfo.getLevel());
            examDTO.setStatus(examInfo.getStatus());
            examDTO.setIntro(examInfo.getIntro());
            examDTO.setExamType(examInfo.getExamType());
            examDTO.setCreatedAt(examInfo.getCreatedAt());
            examDTO.setUpdatedAt(examInfo.getUpdatedAt());
            examDTO.setPrice(examInfo.getPrice());
            examDTO.setCost(examInfo.getCost());
        } else {

            examDTO.setImageUrl("");
            examDTO.setLevel(null);
            examDTO.setStatus(null);
            examDTO.setIntro("");
        }


        Double averageRating = testRepository.findAverageRatingByTestId(test.getId());
        examDTO.setRating(averageRating != null ? averageRating : 0.0);


        int itemCountReview = test.getReviewList() != null ? test.getReviewList().size() : 0;
        examDTO.setItemCountReview(itemCountReview);

        int itemCountPrice = test.getTestEnrollments() != null ? test.getTestEnrollments().size() : 0;
        examDTO.setItemCountPrice(itemCountPrice);

        if (accountId != null) {
            boolean isPurchased = test.getTestEnrollments().stream()
                    .anyMatch(te -> te.getAccount().getId() == accountId);
            examDTO.setPurchased(isPurchased);
        } else {
            examDTO.setPurchased(false);
        }

        Optional<Course_Discount> courseDiscountOpt = courseDiscountRepository.findByTestId(test.getId());
        if (courseDiscountOpt.isPresent() && courseDiscountOpt.get().getDiscount() != null && courseDiscountOpt.get().getDiscount().getStatus() == DiscountStatus.ACTIVE && examInfo.getExamType() == ExamType.FEE) {
            int intValue = courseDiscountOpt.get().getDiscount().getDiscountValue().intValue();
            examDTO.setPercentDiscount(intValue);
        } else {
            examDTO.setPercentDiscount(0);
        }


        return examDTO;
    }


}
