package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminCourseDiscountUpdate;
import com.example.hotrohoctapbackend.service.CourseDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/course-discounts")
public class CourseDiscountController {
    @Autowired
    private CourseDiscountService courseDiscountService;

    @PostMapping("/add-discount/{discountId}")
    public ResponseEntity<String> addDiscountToCourse(
            @PathVariable("discountId") Integer discountId, // Lấy discountId từ URL path
            @RequestParam("courseIds") List<Integer> courseIds) { // Lấy danh sách courseIds từ request
        try {
            // Gọi service để thêm khuyến mãi vào danh sách khóa học
            String response = courseDiscountService.addDiscountToCourses(discountId, courseIds);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi nếu dữ liệu không hợp lệ
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Xử lý các lỗi không mong muốn
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi trong quá trình thêm khuyến mãi: " + e.getMessage());
        }
    }

    @PutMapping("/reset-price")
    public ResponseEntity<String> resetPriceToCost(@RequestParam List<Integer> courseIds) {
        try {
            String response = courseDiscountService.resetPriceToCost(courseIds);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi trong quá trình cập nhật giá: " + e.getMessage());
        }
    }

}
