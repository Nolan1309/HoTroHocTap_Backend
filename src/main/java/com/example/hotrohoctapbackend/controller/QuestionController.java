package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminQuestionGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.DTO.User.QuestionUserExamPayload;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import com.example.hotrohoctapbackend.entity.*;
import com.example.hotrohoctapbackend.service.AccountService;
import com.example.hotrohoctapbackend.service.CourseService;
import com.example.hotrohoctapbackend.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/responsive-test/{testId}")
    public ResponseEntity<List<QuestionResponseDTO_User>> getQuestionsByTestId(@PathVariable Integer testId) {
        List<QuestionResponseDTO_User> questions = questionService.getQuestionsByTestId(testId);
        return ResponseEntity.ok(questions);
    }

    // API để tải lên file Excel
    @PostMapping("/upload")
    public ResponseEntity<String> uploadQuestionsFromExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File không được để trống");
        }

        try {
            questionService.saveQuestionsFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Tải lên thành công");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xử lý file Excel");
        }
    }

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/upload-docx")
    public ResponseEntity<?> uploadQuestions(@RequestParam("file") MultipartFile file,
                                             @RequestParam("dialogType") String dialogType,
                                             @RequestParam("courseId") String courseId,
                                             @RequestParam("accountId") String accountId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File không được để trống");
        }
        List<Question> questions = new ArrayList<>();
        if (dialogType.equals("")) {
            return ResponseEntity.badRequest().body("Loại câu hỏi không được để trống");
        } else if (dialogType.equals("multiple-choice")) {
            questions = questionService.parseDocxFile(file, dialogType);
        } else if (dialogType.equals("essay")) {
            questions = questionService.parseDocxFileEssay(file, dialogType);
        } else if (dialogType.equals("fill-in-the-blank")) {
            questions = questionService.parseDocxFileFill(file, dialogType, courseId, accountId);
        } else if (dialogType.equals("checkbox")) {
            questions = questionService.parseDocxFileCheckbox(file, dialogType);
        } else if (dialogType.equals("mixed")) {
            questions = questionService.importQuestionsFromDocx(file);
        }


        if (questions.isEmpty()) {
            return ResponseEntity.badRequest().body("Không có câu hỏi hợp lệ trong file");
        }

        // Lưu câu hỏi vào DB
        questions.forEach(questionDTO -> {
            Question question = new Question();
            question.setContent(questionDTO.getContent());
            question.setOptionA(questionDTO.getOptionA());
            question.setOptionB(questionDTO.getOptionB());
            question.setOptionC(questionDTO.getOptionC());
            question.setOptionD(questionDTO.getOptionD());
            question.setResult(questionDTO.getResult());
            question.setResult_check(questionDTO.getResult_check());
            question.setInstruction(questionDTO.getInstruction());
            question.setType(questionDTO.getType());
            question.setLevel(questionDTO.getLevel());
            question.setTopic(questionDTO.getTopic());
            question.setCreatedAt(new Date());
            question.setUpdatedAt(new Date());
            Course course = courseRepository.findById(Integer.parseInt(courseId)).get();
            question.setCourse(course);
            Account account = accountRepository.findById(Integer.parseInt(accountId)).get();
            question.setAccount(account);
            questionRepository.save(question);
        });

        return ResponseEntity.ok("Thêm câu hỏi thành công");
    }

    @DeleteMapping
    public String deleteQuestions(@RequestBody List<Integer> ids) {
        try {
            questionService.deleteQuestions(ids);
            return "Deleted successfully.";
        } catch (Exception e) {
            return "Error occurred while deleting questions.";
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportQuestionsToExcel() {
        byte[] excelData = questionService.exportQuestionsToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @PostMapping("/export/excel-list")
    public ResponseEntity<byte[]> exportQuestionsToExcelById(@RequestBody List<String> list) {
        byte[] excelData = questionService.exportQuestionsToExcelByID(list);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions.xlsx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(excelData);
    }

    @PostMapping("/export/docx-list")
    public ResponseEntity<byte[]> exportQuestionsToDocxById(@RequestBody List<String> list) {
        byte[] docxData = questionService.exportQuestionsToDocxByID(list);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=questions.docx");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok().headers(headers).body(docxData);
    }

    @GetMapping("/all")
    public Page<AdminQuestionGetDTO_V2> getAllQuestions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        // Trả về các câu hỏi với phân trang
        return questionService.getAllQuestionsAdmin(page, size);
    }

    @GetMapping("/all-filter")
    public ResponseEntity<Page<AdminQuestionGetDTO_V2>> getQuestionsByFilter(
            @RequestParam(required = false) List<String> topic,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (topic.equals("")) {
            topic = null;
        }
        Page<AdminQuestionGetDTO_V2> questions = questionService.getQuestionsByFilter(topic, courseId, accountId, type, level, content, page, size);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/exam-all-filter")
    public ResponseEntity<Page<AdminQuestionGetDTO_V2>> getQuestionsByFilterExam(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AdminQuestionGetDTO_V2> questions = questionService.getQuestionsByFilterExam(courseId, accountId, type, level, content, page, size);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/all-filter-bank")
    public ResponseEntity<Page<AdminQuestionGetDTO_V2>> getQuestionsByFilterBank(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (topic.equals("")) {
            topic = null;
        }
        Page<AdminQuestionGetDTO_V2> questions = questionService.getQuestionsByFilterBank(topic, courseId, accountId, type, level, content, page, size);
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/all-list")
    public List<AdminQuestionGetDTO> getAllQuestionsList() {
        // Trả về các câu hỏi với phân trang
        return questionService.getAllQuestionsAdminList();
    }


    @GetMapping("/tests/questions/{testId}")
    public Page<Question> getQuestionsByTestIdAdmin(
            @PathVariable Integer testId,
            @RequestParam(defaultValue = "0") int page, // Default page is 0
            @RequestParam(defaultValue = "10") int size // Default size is 10
    ) {
        return questionService.getQuestionsByTestIdAdmin(testId, page, size);
    }


    @GetMapping("detail/{id}")
    public ResponseEntity<AdminQuestionGetDTO> getQuestionDetailsById(@PathVariable int id) {
        AdminQuestionGetDTO questionDTO = questionService.getQuestionDetailsByIdAdmin(id);
        if (questionDTO != null) {
            return ResponseEntity.ok(questionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody AdminQuestionMultiPostDTO_V2 adminQuestionGetDTO) {
        try {
            if (adminQuestionGetDTO.getType().equals("fill-in-the-blank")) {
                questionService.addQuestionFillAdmin(adminQuestionGetDTO);
                return new ResponseEntity<>("Thêm câu hỏi thành công", HttpStatus.CREATED);
            } else if (adminQuestionGetDTO.getType().equals("multiple-choice")) {
                questionService.addQuestionMultiAdmin(adminQuestionGetDTO);
                return new ResponseEntity<>("Thêm câu hỏi thành công", HttpStatus.CREATED);
            } else {
                questionService.addQuestionEssayAdmin(adminQuestionGetDTO);
                return new ResponseEntity<>("Thêm câu hỏi thành công", HttpStatus.CREATED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi thêm câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-checkbox")
    public ResponseEntity<?> addCheckboxQuestion(@RequestBody CheckboxQuestionDTO_V2 questionDTO) {
        questionService.addQuestionCheckboxAdmin(questionDTO);
        return ResponseEntity.ok("Câu hỏi Checkbox được thêm thành công!");
    }

    @GetMapping("/detail/checkbox")
    public ResponseEntity<?> getCheckboxQuestion(@RequestParam Long id, @RequestParam String type) {
        return questionService.getCheckboxQuestionByIdAndType(id, type).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQuestion(@PathVariable int id, @RequestBody AdminQuestionGetDTO adminQuestionGetDTO) {
        try {
            boolean updated = questionService.updateQuestionAdmin(id, adminQuestionGetDTO);
            if (updated) {
                return new ResponseEntity<>("Cập nhật câu hỏi thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-v2/{id}")
    public ResponseEntity<String> updateQuestion_V2(@PathVariable int id, @RequestBody AdminQuestionGetDTO_V2 adminQuestionGetDTO) {
        try {

            boolean updated = questionService.updateQuestionAdminV2(id, adminQuestionGetDTO);


            if (updated) {
                return new ResponseEntity<>("Cập nhật câu hỏi thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-v2-checkbox/{id}")
    public ResponseEntity<String> updateQuestion_V2_Checkbox(@PathVariable int id, @RequestBody CheckboxQuestionDTO_V3 adminQuestionGetDTO) {
        try {

            boolean updated = questionService.updateQuestionAdminV2_Checkbox(id, adminQuestionGetDTO);
            if (updated) {
                return new ResponseEntity<>("Cập nhật câu hỏi thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không tìm thấy câu hỏi", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Có lỗi xảy ra khi cập nhật câu hỏi", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/copy-to-course")
    public ResponseEntity<String> copyQuestionsToCourse(@RequestBody CopyQuestionsRequest request) {
        try {
            boolean success = questionService.copyQuestionsToCourse(request.getQuestionIds(), request.getTargetCourseId());

            if (success) {
                return ResponseEntity.ok("Các câu hỏi đã được sao chép thành công vào khóa học ID: " + request.getTargetCourseId());
            } else {
                return ResponseEntity.status(500).body("Có lỗi xảy ra khi sao chép các câu hỏi.");
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Đã xảy ra lỗi nội bộ.");
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> deleteTestAdmin(@PathVariable int id) {
        try {
            Question deletedQuestion = questionService.deleteQuestionAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> activeQuestionAdmin(@PathVariable int id) {
        try {
            Question activedQuestion = questionService.activeQuestionAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @GetMapping("/restore/list-all-questions")
    public Page<AdminQuestionDTORestoreList> getQuestions(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (content.equals("")) {
            content = null;
        }
        if (deletedDate.equals("")) {
            deletedDate = null;
        }

        return questionService.getQuestions(courseId, accountId, content, deletedDate, page, size);
    }

    @PutMapping("/restore/{questionId}")
    public ResponseEntity<Question> restoreQuestion(@PathVariable Integer questionId) {
        AdminQuestionDTORestoreList adminQuestionDTORestoreList = new AdminQuestionDTORestoreList();
        adminQuestionDTORestoreList.setId(questionId);
        Question restoreQuestion = questionService.updateRestoreQuestion(adminQuestionDTORestoreList);
        return ResponseEntity.ok(restoreQuestion);
    }

    @DeleteMapping("/delete/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer questionId) {
        AdminQuestionDTORestoreList adminQuestionDTORestoreList = new AdminQuestionDTORestoreList();
        adminQuestionDTORestoreList.setId(questionId);
        questionService.deleteRestoreQuestion(adminQuestionDTORestoreList);
        return ResponseEntity.ok("Question permanently deleted.");
    }

    @PostMapping("/count-course")
    public List<QuestionCountDTO> getQuestionsCountByLevel(@RequestParam(required = false) Integer chapterID) {
        return questionService.getQuestionsCountByLevel(chapterID);
    }


}
