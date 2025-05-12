package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.CourseData_Excel;
import com.example.hotrohoctapbackend.DTO.Admin.CourseReportDTO;
import com.example.hotrohoctapbackend.service.CourseService;
import com.example.hotrohoctapbackend.service.PaymentsService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private PaymentsService paymentsService;

    @GetMapping("/export-revenue-report")
    public void exportRevenueReport(HttpServletResponse response) throws IOException {
        // Lấy dữ liệu khóa học
        List<CourseReportDTO> validCourses = courseService.getCourseReport();

        // Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh Thu Theo Khóa");

        // Tạo tiêu đề quốc hiệu và tiêu ngữ
        Row header1 = sheet.createRow(1);
        Row header2 = sheet.createRow(2);

        // Tạo ô và đặt giá trị
        Cell cell1 = header1.createCell(3);
        cell1.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");

        Cell cell2 = header2.createCell(3);
        cell2.setCellValue("Độc lập - Tự do - Hạnh phúc");

        // Căn giữa
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        centerStyle.setFont(boldFont);

        cell1.setCellStyle(centerStyle);
        cell2.setCellStyle(centerStyle);

        // Tạo tiêu đề báo cáo
        Row reportTitle = sheet.createRow(3);
        reportTitle.createCell(3).setCellValue("BÁO CÁO DOANH THU KHÓA HỌC");
        reportTitle.getCell(3).setCellStyle(centerStyle);

        // Ngày tháng
        Row dateRow = sheet.createRow(4);
        dateRow.createCell(4).setCellValue("Ngày ........ tháng ........ năm ........");

        // Tạo tiêu đề cho các cột
        Row header = sheet.createRow(7);
        header.createCell(1).setCellValue("STT");
        header.createCell(2).setCellValue("Tên khóa học");
        header.createCell(3).setCellValue("Số học viên");
        header.createCell(4).setCellValue("Trạng thái");
        header.createCell(5).setCellValue("Doanh thu");

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font boldFont1 = workbook.createFont();
        boldFont1.setBold(true);
        headerStyle.setFont(boldFont1);

// Thiết lập border cho style
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

// Áp dụng style có border cho các ô tiêu đề
        for (int i = 1; i <= 5; i++) {
            Cell cell = header.getCell(i);
            if (cell == null) cell = header.createCell(i);
            cell.setCellStyle(headerStyle);
        }

        // Thêm dữ liệu vào sheet
        int rowNum = 8; // Dòng bắt đầu ghi dữ liệu
        int stt = 1;    // Số thứ tự bắt đầu từ 1

        DataFormat format = workbook.createDataFormat();
        CellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(format.getFormat("#,##0 \"VND\""));
        currencyStyle.setAlignment(HorizontalAlignment.RIGHT);
        currencyStyle.setBorderTop(BorderStyle.THIN);
        currencyStyle.setBorderBottom(BorderStyle.THIN);
        currencyStyle.setBorderLeft(BorderStyle.THIN);
        currencyStyle.setBorderRight(BorderStyle.THIN);
        for (CourseReportDTO course : validCourses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(1).setCellValue(stt++); // Cột STT
            row.createCell(2).setCellValue(course.getCourseName());
            row.createCell(3).setCellValue(course.getStudents() != null ? course.getStudents() : 0);
            row.createCell(4).setCellValue(course.getStatus() ? "Đang hoạt động" : "Ngừng hoạt động");
            Cell revenueCell = row.createCell(5);
            BigDecimal revenue1 = course.getRevenue();
            revenueCell.setCellValue(revenue1 != null ? revenue1.doubleValue() : 0.0);

            revenueCell.setCellStyle(currencyStyle);


            for (int col = 1; col <= 5; col++) {
                Cell cell = row.getCell(col);
                if (cell == null) cell = row.createCell(col); // Đảm bảo ô tồn tại
                CellStyle borderStyle = workbook.createCellStyle();
                borderStyle.setBorderTop(BorderStyle.THIN);
                borderStyle.setBorderBottom(BorderStyle.THIN);
                borderStyle.setBorderLeft(BorderStyle.THIN);
                borderStyle.setBorderRight(BorderStyle.THIN);
                borderStyle.setAlignment(HorizontalAlignment.CENTER);
                cell.setCellStyle(borderStyle);
            }
        }

// Chia đều độ rộng các cột
        sheet.setColumnWidth(1, 2500); // STT
        sheet.setColumnWidth(2, 8000); // Tên khóa học
        sheet.setColumnWidth(3, 5000); // Số học viên
        sheet.setColumnWidth(4, 7000); // Trạng thái
        sheet.setColumnWidth(5, 7000); // Doanh thu


        // Phần ký tên
        Row signRow = sheet.createRow(rowNum + 2);
        signRow.createCell(4).setCellValue("Người lập báo cáo");
        CellStyle signStyle = workbook.createCellStyle();
        signStyle.setAlignment(HorizontalAlignment.CENTER);
        signRow.getCell(4).setCellStyle(signStyle);

        // Thiết lập response để tải file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=doanh_thu_theo_khoa.xlsx");

        // Ghi dữ liệu vào output stream
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/export-year")
    public ResponseEntity<byte[]> exportReport(@RequestParam int year) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh Thu NĂM");

        // Tạo quốc hiệu và tiêu ngữ
        Row header1 = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
        Cell countryCell = header1.createCell(0);
        countryCell.setCellValue("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM");
        CellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        centerStyle.setFont(boldFont);
        countryCell.setCellStyle(centerStyle);

        Row header2 = sheet.createRow(1);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
        Cell mottoCell = header2.createCell(0);
        mottoCell.setCellValue("Độc lập - Tự do - Hạnh phúc");
        mottoCell.setCellStyle(centerStyle);

        // Tiêu đề báo cáo
        Row reportTitle = sheet.createRow(3);
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
        Cell titleCell = reportTitle.createCell(0);
        titleCell.setCellValue("BÁO CÁO DOANH THU NĂM " + year);
        titleCell.setCellStyle(centerStyle);

        // Ngày tháng
        Row dateRow = sheet.createRow(5);
        dateRow.createCell(1).setCellValue("Ngày ........ tháng ........ năm ........");

        // Tạo header cho bảng dữ liệu
        String[] headers = {"Mã Tháng", "Doanh thu (VND)"};
        Row headerRow = sheet.createRow(7);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setFont(boldFont);
            cell.setCellStyle(style);
        }

        // Lấy dữ liệu từ service
        List<Object[]> list = paymentsService.getMonthlySalesData(year);

        // Chuẩn bị dữ liệu tháng 1-12 với mặc định 0
        Map<Integer, Double> monthlyRevenueMap = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            monthlyRevenueMap.put(month, 0.0);
        }

        // Cập nhật dữ liệu vào Map
        for (Object[] rowData : list) {
            int month = Integer.parseInt(rowData[1].toString());
            double revenue = Double.parseDouble(rowData[3].toString());
            monthlyRevenueMap.put(month, revenue);
        }

        // Ghi dữ liệu vào Excel
        int rowNum = 8;
        for (int month = 1; month <= 12; month++) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("T" + month);

            Cell revenueCell = row.createCell(1);
            revenueCell.setCellValue(monthlyRevenueMap.get(month));

            // Định dạng VND
            CellStyle currencyStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0 \"VND\""));
            currencyStyle.setAlignment(HorizontalAlignment.RIGHT);
            currencyStyle.setBorderTop(BorderStyle.THIN);
            currencyStyle.setBorderBottom(BorderStyle.THIN);
            currencyStyle.setBorderLeft(BorderStyle.THIN);
            currencyStyle.setBorderRight(BorderStyle.THIN);
            revenueCell.setCellStyle(currencyStyle);
        }

        // Xuất file Excel
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("attachment", "doanh_thu_theo_thang.xlsx");

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(outputStream.toByteArray());
    }


}
