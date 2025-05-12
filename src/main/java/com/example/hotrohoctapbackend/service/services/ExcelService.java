package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDetailDTO;
import com.example.hotrohoctapbackend.service.StudentCourseDataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.io.FileOutputStream;

@Service
public class ExcelService {

    @Autowired
    private StudentCourseDataService studentCourseDataService;

    public byte[] exportToExcel(Integer courseId, List<String> classRooms) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Tạo Sheet 1
        Sheet sheet1 = workbook.createSheet("Danh Sách Sinh Viên");

        // Thêm tiêu đề cho sheet 1
        Row headerRow1 = sheet1.createRow(0);
        headerRow1.createCell(0).setCellValue("MSSV");
        headerRow1.createCell(1).setCellValue("Tên");
        headerRow1.createCell(2).setCellValue("Lớp");
        headerRow1.createCell(3).setCellValue("Tuổi");
        headerRow1.createCell(4).setCellValue("Giới tính");
        headerRow1.createCell(5).setCellValue("Tiến độ (%)");
        headerRow1.createCell(6).setCellValue("Điểm thi");
        headerRow1.createCell(7).setCellValue("Dự đoán");

        // Lấy danh sách sinh viên từ API
        List<StudentExportStudentHuitDTO> students = studentCourseDataService.getAllStudentHuitByCourseIdAndResultPredictionListByListClass(courseId, classRooms);

        // Thêm dữ liệu sinh viên vào sheet 1
        int rowIndex = 1;
        for (StudentExportStudentHuitDTO student : students) {
            Row row = sheet1.createRow(rowIndex++);
            row.createCell(0).setCellValue(student.getStudentId());
            row.createCell(1).setCellValue(student.getFullname());
            row.createCell(2).setCellValue(student.getClassRoom());
            row.createCell(3).setCellValue(student.getAge());
            String gender = student.getGender();
            if (gender.equals("0")) {
                row.createCell(4).setCellValue("Nữ");
            } else row.createCell(4).setCellValue("Nam");

            row.createCell(5).setCellValue(student.getAssignmentCompletionRate());


            double examScore = student.getExamScore();
            String formattedScore = String.format("%.1f", examScore);
            if (formattedScore.equals("0.0")) {
                row.createCell(6).setCellValue("0");
            } else row.createCell(6).setCellValue(formattedScore);


            String prediction = "Chưa rõ";
            if (student.getPrediction() != null) {
                if (student.getPrediction().equals("0")) {
                    prediction = "Qua môn";
                } else if (student.getPrediction().equals("1")) {
                    prediction = "Rớt môn";
                }
            }
            row.createCell(7).setCellValue(prediction);
        }

        // Tạo Sheet 2
        Sheet sheet2 = workbook.createSheet("Chi Tiết Điểm Sinh Viên");

        // Thêm tiêu đề cho sheet 2
        Row headerRow2 = sheet2.createRow(0);
        headerRow2.createCell(0).setCellValue("MSSV");
        headerRow2.createCell(1).setCellValue("Tên");
        headerRow2.createCell(2).setCellValue("Chương");
        headerRow2.createCell(3).setCellValue("Quiz Chương");
        headerRow2.createCell(4).setCellValue("Bài học");
        headerRow2.createCell(5).setCellValue("Lần làm");
        headerRow2.createCell(6).setCellValue("Quiz Bài học");
        headerRow2.createCell(7).setCellValue("Thời gian");
        rowIndex = 1;
        // Lấy chi tiết điểm từ API
        for (StudentExportStudentHuitDTO student : students) {
            List<StudentExportStudentHuitDetailDTO> details = studentCourseDataService.getAllStudentHuitDetailByCourseIdAndResultPredictionListByClassRooms(courseId, Integer.parseInt(student.getAccountId()), student.getStudentId());

            // Thêm dữ liệu chi tiết vào sheet 2
            for (StudentExportStudentHuitDetailDTO detail : details) {
                Row row = sheet2.createRow(rowIndex++);
                row.createCell(0).setCellValue(detail.getStudentId());
                row.createCell(1).setCellValue(detail.getFullname());
                row.createCell(2).setCellValue(detail.getChapterTitle());
                row.createCell(3).setCellValue(detail.getChapterQuiz());
                row.createCell(4).setCellValue(detail.getLessonTitle());
                String lessonQuiz = detail.getLessonQuiz();
                int countTime = detail.getCountTime();
                if ("Chưa có điểm".equals(lessonQuiz)) {
                    countTime = 0;
                }
                row.createCell(5).setCellValue(countTime);
                row.createCell(6).setCellValue(lessonQuiz);


                String createDateString = detail.getCreateDate();
                if ("Chưa có thời gian".equals(createDateString)) {
                    row.createCell(7).setCellValue(createDateString);
                } else {
                    String formattedDate = createDateString.split(" ")[0];
                    row.createCell(7).setCellValue(formattedDate);
                }
            }
        }

        // Ghi file Excel vào OutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Trả về file Excel dưới dạng byte array
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=student_data.csv");

        return outputStream.toByteArray();
    }
}
