package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentCourseDataHuitDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.PredictionResultDTO;
import com.example.hotrohoctapbackend.dao.PredictionResultRepository;
import com.example.hotrohoctapbackend.entity.PredictionResult;
import com.example.hotrohoctapbackend.entity.StudentCourseData;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.StudentCourseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/prediction-result")
public class PredictionResultController {
    @Autowired
    private PredictionResultRepository predictionResultRepository;

    @Autowired
    private StudentCourseDataService studentCourseDataService;

    // API để hiển thị kết quả dự đoán của sinh viên
    @GetMapping("/student")
    public ResponseEntity<ApiResponse<?>> getPredictionByAccountId(@RequestParam Integer accountId) {
        try {
            Optional<PredictionResult> predictionResultOpt =
                    predictionResultRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId);

            if (predictionResultOpt.isPresent()) {
                PredictionResult predictionResult = predictionResultOpt.get();

                PredictionResultDTO dto = new PredictionResultDTO();
                dto.setAccountId(predictionResult.getAccount().getId());
                dto.setStudentId(predictionResult.getStudentId());
                dto.setCluster(predictionResult.getCluster());
                dto.setClusterDescription(predictionResult.getClusterDescription());
                dto.setClusterLabel(predictionResult.getClusterLabel());
                dto.setLearningPathSuggestion(predictionResult.getLearningPathSuggestion());
                dto.setPrediction(predictionResult.getPrediction());
                dto.setProbability(predictionResult.getProbability());
                dto.setRiskLevel(predictionResult.getRiskLevel());
                dto.setCreatedAt(predictionResult.getCreatedAt());

                ApiResponse<PredictionResultDTO> response = new ApiResponse<>(200, "Lấy dữ liệu thành công", dto);
                return ResponseEntity.ok(response); // Luôn HTTP 200
            } else {
                // Trả HTTP 200 nhưng báo lỗi logic trong body
                ApiResponse<Object> response = new ApiResponse<>(404, "Dự đoán không tìm thấy cho tài khoản này", null);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            // Bắt mọi lỗi khác, trả HTTP 200 nhưng status = 500
            ApiResponse<Object> response = new ApiResponse<>(500, "Lỗi hệ thống: " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/student-huit-item")
    public ResponseEntity<ApiResponse<?>> getInformationStudentByAccountId(@RequestParam Integer id) {
        try {
            Optional<StudentCourseData> studentOpt = studentCourseDataService.getStudentByAccountIdAndCourseId(id, 1);

            if (studentOpt.isPresent()) {
                StudentCourseData studentData = studentOpt.get();

                // Chuyển dữ liệu từ entity sang DTO
                StudentCourseDataHuitDTO dto = new StudentCourseDataHuitDTO();
                dto.setStudentId(studentData.getStudentId());
                dto.setAge(studentData.getAge());
                dto.setEmail(studentData.getEmail());
                dto.setFullName(studentData.getFullname());

                // Kiểm tra giới tính
                String gender = studentData.getGender() == 1 ? "Nam" : "Nữ";
                dto.setGender(gender);

                // Trả về kết quả với mã 200
                ApiResponse<StudentCourseDataHuitDTO> response = new ApiResponse<>(200, "Successful", dto);
                return ResponseEntity.ok(response);
            } else {
                // Trả về lỗi 404 nếu không tìm thấy sinh viên
                ApiResponse<Object> response = new ApiResponse<>(404, "Sinh viên không tồn tại!", null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            // Xử lý lỗi và trả về 500
            ApiResponse<Object> response = new ApiResponse<>(500, "Lỗi hệ thống: " + e.getMessage(), null);
            return ResponseEntity.ok(response);
        }
    }

}
