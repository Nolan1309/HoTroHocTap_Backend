package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.CourseCode.CourseCodeCreate;
import com.example.hotrohoctapbackend.DTO.User.CourseCodeActivationRequest;
import com.example.hotrohoctapbackend.DTO.User.StudentBehaviorRequestDTO;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.CourseCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/course-codes")
public class CourseCodeController {

    @Autowired
    private CourseCodeService courseCodeService;

    // API tạo mã khóa học
    @PostMapping("/create")
    public List<CourseCodeResponseDTO> createCourseCodes(
            @RequestBody CourseCodeCreateDTO courseCodeCreateDTO) {

        return courseCodeService.createCourseCodes(courseCodeCreateDTO.getQuantity(), courseCodeCreateDTO.getCourseId(),
                courseCodeCreateDTO.getAccountId(), courseCodeCreateDTO.getExpiryDays());
    }

    @GetMapping("/list/{courseId}")
    public Page<CourseCodeResponseDTO> getCourseCodes(
            @PathVariable Integer courseId,
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "50") int size, // Kích thước mặc định là 50
            @RequestParam(defaultValue = "") String codeSearch // Tìm kiếm theo mã code (mặc định là không tìm kiếm)
    ) {
        return courseCodeService.getCourseCodes(courseId, page, size, codeSearch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseCode(@PathVariable int id) {
        try {
            courseCodeService.deleteCourseCode(id);  // Call service to delete CourseCode by ID
            return ResponseEntity.noContent().build();  // Return HTTP 204 (No Content) if successful
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Return error if something goes wrong
        }
    }

    @PostMapping("/enable")
    public ResponseEntity<String> activateCourseCode(@RequestBody StudentBehaviorRequestDTO request) {
        try {
            String message = courseCodeService.activateCourseCode(request, request.getCode(), request.getAccountId());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/enable-not-huit")
    public ResponseEntity<String> activateCourseCodeNotHuit(@RequestBody CourseCodeCreate request) {
        try {
            String message = courseCodeService.activateCourseCodeNotHuit(request.getCode(), request.getAccountId());
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/check-enable")
    public ResponseEntity<CourseCodeStatusResponse> checkCourseCode(@RequestBody CourseCodeActivationRequest request) {
        try {
            // Call service to check the course code
            CourseCodeStatusResponse response = courseCodeService.checkCourseCode(request.getCode());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CourseCodeStatusResponse(false, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ApiResponse<CourseCodeResponseDTO> updateCourseCode(
            @PathVariable int id,
            @RequestBody CourseCodeRequestDTO courseCodeRequestDTO) {
        try {
            ApiResponse<CourseCodeResponseDTO> updatedCourseCode = courseCodeService.updateCourseCode(id, courseCodeRequestDTO);
            return updatedCourseCode;
        } catch (Exception e) {

            return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        }
    }

    @PostMapping("/send-course-codes")
    public ResponseEntity<String> sendCourseCodes(@RequestBody AdminCourseCodeRequestEmailDTO request) {
        try {
            courseCodeService.sendCourseCodesToSelectedStudents(request.getSelectedStudents(), request.getCourseId(), request.getAccountId());
            return ResponseEntity.ok("Course codes sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send course codes: " + e.getMessage());
        }
    }

}