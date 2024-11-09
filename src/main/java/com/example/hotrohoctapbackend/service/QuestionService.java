package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.entity.Question;
import com.example.hotrohoctapbackend.dao.QuestionRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

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

            question.setContent(getCellValue(row.getCell(0)));
            question.setInstruction(getCellValue(row.getCell(1)));
            question.setOptionA(getCellValue(row.getCell(2)));
            question.setOptionB(getCellValue(row.getCell(3)));
            question.setOptionC(getCellValue(row.getCell(4)));
            question.setOptionD(getCellValue(row.getCell(5)));
            question.setResult(getCellValue(row.getCell(6)));

            questionList.add(question);
        }

        workbook.close();

        // Lưu tất cả câu hỏi vào cơ sở dữ liệu
        questionRepository.saveAll(questionList);
    }

    // Phương thức hỗ trợ để lấy giá trị của cell
    private String getCellValue(Cell cell) {
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
        List<Question> questions = getAllQuestions();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Questions");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Content", "Instruction", "Option A", "Option B", "Option C", "Option D", "Result", "Created At", "Updated At"};

            // Tạo dòng tiêu đề
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            // Ghi dữ liệu vào từng dòng
            int rowNum = 1;
            for (Question question : questions) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(question.getId());
                row.createCell(1).setCellValue(question.getContent());
                row.createCell(2).setCellValue(question.getInstruction());
                row.createCell(3).setCellValue(question.getOptionA());
                row.createCell(4).setCellValue(question.getOptionB());
                row.createCell(5).setCellValue(question.getOptionC());
                row.createCell(6).setCellValue(question.getOptionD());
                row.createCell(7).setCellValue(question.getResult());
                row.createCell(8).setCellValue(question.getCreatedAt().toString());
                row.createCell(9).setCellValue(question.getUpdatedAt().toString());
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
    public List<Question> getQuestionsByTestId(Integer testId) {
        return questionRepository.findQuestionsByTestId(testId);
    }
}
