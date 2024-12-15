package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.CourseData_Excel;
import com.example.hotrohoctapbackend.DTO.Admin.CourseReportDTO;
import com.example.hotrohoctapbackend.service.CourseService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private CourseService courseService;
    @GetMapping("/export-revenue-report")
    public void exportRevenueReport(HttpServletResponse response) throws IOException {
        // Lấy dữ liệu khóa học
//        List<CourseReportDTO> courses = courseService.getCourseData();

        // Lọc các khóa học hợp lệ
        List<CourseReportDTO> validCourses = courseService.getCourseReport();

        // Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh Thu Theo Năm");

        // Tạo tiêu đề cho các cột
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên khóa học");
        header.createCell(1).setCellValue("Số học viên");
        header.createCell(2).setCellValue("Doanh thu");

        // Thêm dữ liệu vào sheet
        int rowNum = 1;
        for (CourseReportDTO course : validCourses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(course.getCourseName());
            row.createCell(1).setCellValue(course.getStudents() != null ? course.getStudents() : 0);
            BigDecimal revenue = course.getRevenue();
            row.createCell(2).setCellValue(revenue != null ? revenue.doubleValue() : 0.0);
        }

        // Thiết lập response để tải file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=doanh_thu_theo_nam.xlsx");

        // Ghi dữ liệu vào output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
