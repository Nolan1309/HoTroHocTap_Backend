package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminQuestionGetDTO;
import com.example.hotrohoctapbackend.DTO.User.QuestionDTO_User;
import com.example.hotrohoctapbackend.DTO.User.QuestionResponseDTO_User;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.entity.Test;
import org.springframework.data.domain.Page;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionDTO_User> getQuestionsByTestId(int testId) {
        // Lấy danh sách câu hỏi theo testId và chuyển đổi sang QuestionDTO_User
        List<Object[]> results = questionRepository.findQuestionsByTestId(testId);
        return results.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    //Get result check for test after submit from user
    public List<QuestionResponseDTO_User> getQuestionsByTestId(Integer testId) {
        List<Object[]> list = questionRepository.findQuestionsResponsiveByTestId(testId);
        List<QuestionResponseDTO_User> list_answer = new ArrayList<>();
        for (Object[] item : list) {
            QuestionResponseDTO_User questionResponse = new QuestionResponseDTO_User();
            questionResponse.setId((Integer) item[0]);
            questionResponse.setInstruction((String) item[1]);
            questionResponse.setCorrect_show((String) item[2]);
            questionResponse.setCorrect_check((String) item[3]);
            list_answer.add(questionResponse);
        }
        return list_answer;
    }

    //    SELECT q.id AS questionId, q.instruction AS instruction, q.result AS correctShow, q.result_check AS correctCheck
    private QuestionDTO_User convertToDTO(Object[] result) {
        QuestionDTO_User dto = new QuestionDTO_User();
        dto.setQuestionId((Integer) result[0]);
        dto.setContent((String) result[1]);
        dto.setOptionA((String) result[2]);
        dto.setOptionB((String) result[3]);
        dto.setOptionC((String) result[4]);
        dto.setOptionD((String) result[5]);
        dto.setCreatedAt((Date) result[6]);
        dto.setUpdatedAt((Date) result[7]);
        return dto;
    }

    public void saveQuestionsFromExcel(MultipartFile file) throws IOException {
        List<Question> questionList = new ArrayList<>();

        // Tạo Workbook từ file Excel
        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên

        // Lặp qua các dòng trong sheet
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Bỏ qua hàng tiêu đề (header)
            if (row.getRowNum() == 0) {
                continue;
            }

            Question question = new Question();

            // Set thông tin câu hỏi từ file Excel
            question.setContent(getCellValue(row.getCell(0))); // Content
            question.setInstruction(getCellValue(row.getCell(1))); // Instruction
            question.setOptionA(getCellValue(row.getCell(2))); // Option A
            question.setOptionB(getCellValue(row.getCell(3))); // Option B
            question.setOptionC(getCellValue(row.getCell(4))); // Option C
            question.setOptionD(getCellValue(row.getCell(5))); // Option D
            String resultCheck = getCellValue(row.getCell(6)); // Result check (A, B, C, D)

            // Xử lý kết quả câu hỏi dựa vào Result Check
            if ("A".equals(resultCheck)) {
                question.setResult(question.getOptionA()); // Set đáp án đúng (Option A)
                question.setResult_check(resultCheck); // Set Result check là A
            } else if ("B".equals(resultCheck)) {
                question.setResult(question.getOptionB()); // Set đáp án đúng (Option B)
                question.setResult_check(resultCheck); // Set Result check là B
            } else if ("C".equals(resultCheck)) {
                question.setResult(question.getOptionC()); // Set đáp án đúng (Option C)
                question.setResult_check(resultCheck); // Set Result check là C
            } else if ("D".equals(resultCheck)) {
                question.setResult(question.getOptionD()); // Set đáp án đúng (Option D)
                question.setResult_check(resultCheck); // Set Result check là D
            }

            // Set thêm thông tin tạo và cập nhật
            question.setCreatedAt(new Date());
            question.setUpdatedAt(new Date());

            // Thêm câu hỏi vào danh sách
            questionList.add(question);
        }

        workbook.close();

        // Lưu tất cả câu hỏi vào cơ sở dữ liệu
        questionRepository.saveAll(questionList);
    }



    // Phương thức hỗ trợ để lấy giá trị của cell
    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


    public void deleteQuestions(List<Integer> ids) {
        questionRepository.deleteQuestionsByIds(ids);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public byte[] exportQuestionsToExcel() {
        List<Question> questions = getAllQuestions(); // Lấy tất cả câu hỏi từ database

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Questions");

            // Tạo dòng tiêu đề
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Content", "Instruction", "Option A", "Option B", "Option C", "Option D", "Result Check"};

            // Ghi tiêu đề vào từng cột
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Ghi dữ liệu câu hỏi vào các dòng
            int rowNum = 1;
            for (Question question : questions) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(question.getContent()); // Content
                row.createCell(1).setCellValue(question.getInstruction()); // Instruction
                row.createCell(2).setCellValue(question.getOptionA()); // Option A
                row.createCell(3).setCellValue(question.getOptionB()); // Option B
                row.createCell(4).setCellValue(question.getOptionC()); // Option C
                row.createCell(5).setCellValue(question.getOptionD()); // Option D

                // Cột Result Check (A, B, C, D)
                String resultCheck = question.getResult_check(); // Lấy giá trị của result_check
                row.createCell(6).setCellValue(resultCheck); // Result Check
            }

            // Ghi Workbook vào byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Có lỗi xảy ra khi xuất dữ liệu ra file Excel");
        }
    }



    public Page<Question> getQuestionsByTestIdAdmin(Integer testId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Create a Pageable object
        return questionRepository.findQuestionsByTestIdAdmin(testId, pageable);
    }

    public AdminQuestionGetDTO getQuestionDetailsByIdAdmin(int id) {
        return questionRepository.getQuestionDetailsById(id).stream()
                .map(result -> new AdminQuestionGetDTO(
                        (int) result[0],                // id
                        (String) result[1],              // content
                        (String) result[2],              // optionA
                        (String) result[3],              // optionB
                        (String) result[4],              // optionC
                        (String) result[5],              // optionD
                        (String) result[6],              // result
                        (String) result[7],              // instruction
                        (String) result[8]               // resultCheck
                ))
                .findFirst()
                .orElse(null);
    }

    public boolean updateQuestionAdmin(int id, AdminQuestionGetDTO adminQuestionGetDTO) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return false;
        }
        question.setContent(adminQuestionGetDTO.getContent());
        question.setOptionA(adminQuestionGetDTO.getOptionA());
        question.setOptionB(adminQuestionGetDTO.getOptionB());
        question.setOptionC(adminQuestionGetDTO.getOptionC());
        question.setOptionD(adminQuestionGetDTO.getOptionD());
        question.setResult(adminQuestionGetDTO.getResult());
        question.setInstruction(adminQuestionGetDTO.getInstruction());
        question.setResult_check(adminQuestionGetDTO.getResultCheck());
        question.setUpdatedAt(new Date());
        questionRepository.save(question);
        return true;
    }

    public void addQuestionAdmin(AdminQuestionGetDTO adminQuestionGetDTO) {
        Question question = new Question();
        question.setContent(adminQuestionGetDTO.getContent());
        question.setOptionA(adminQuestionGetDTO.getOptionA());
        question.setOptionB(adminQuestionGetDTO.getOptionB());
        question.setOptionC(adminQuestionGetDTO.getOptionC());
        question.setOptionD(adminQuestionGetDTO.getOptionD());

        if (adminQuestionGetDTO.getResultCheck().equals("A")) {
            question.setResult(adminQuestionGetDTO.getOptionA());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else if (adminQuestionGetDTO.getResultCheck().equals("B")) {
            question.setResult(adminQuestionGetDTO.getOptionB());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else if (adminQuestionGetDTO.getResultCheck().equals("C")) {
            question.setResult(adminQuestionGetDTO.getOptionC());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        } else {
            question.setResult(adminQuestionGetDTO.getOptionD());
            question.setResult_check(adminQuestionGetDTO.getResultCheck());
        }

        question.setInstruction(adminQuestionGetDTO.getInstruction());

        question.setCreatedAt(new Date());
        question.setUpdatedAt(new Date());

        questionRepository.save(question);
    }
    public Page<AdminQuestionGetDTO> getAllQuestionsAdmin(int page, int size) {
        // Tạo đối tượng Pageable từ page và size
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để lấy dữ liệu phân trang
        Page<Object[]> result = questionRepository.getAllQuestions(pageable);

        // Chuyển đổi từ Object[] sang DTO
        List<AdminQuestionGetDTO> dtoList = result.getContent().stream()
                .map(row -> new AdminQuestionGetDTO(
                        (Integer) row[0],  // questionId
                        (String) row[1],   // content
                        (String) row[2],   // optionA
                        (String) row[3],   // optionB
                        (String) row[4],   // optionC
                        (String) row[5],   // optionD
                        (String) row[6],   // result
                        (String) row[7],   // instruction
                        (String) row[8]    // resultCheck
                ))
                .collect(Collectors.toList());

        // Trả về Page<AdminQuestionGetDTO>
        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }

    public List<AdminQuestionGetDTO> getAllQuestionsAdminList() {

        // Gọi repository để lấy dữ liệu phân trang
        List<Object[]> result = questionRepository.getAllQuestionsList();

        List<AdminQuestionGetDTO> dtoList = result.stream()
                .map(row -> new AdminQuestionGetDTO(
                        (Integer) row[0],  // questionId
                        (String) row[1],   // content
                        (String) row[2],   // optionA
                        (String) row[3],   // optionB
                        (String) row[4],   // optionC
                        (String) row[5],   // optionD
                        (String) row[6],   // result
                        (String) row[7],   // instruction
                        (String) row[8]    // resultCheck
                ))
                .collect(Collectors.toList());

        // Trả về List<AdminQuestionGetDTO>
        return dtoList;
    }


    public Question deleteQuestionAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Question> accountOpt = questionRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Question account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return questionRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + testID);
        }
    }

    public Question activeQuestionAdmin(int testID) {
        // Tìm tài khoản theo ID
        Optional<Question> accountOpt = questionRepository.findById(testID);

        if (accountOpt.isPresent()) {
            Question account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return questionRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + testID);
        }
    }
}