package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentCourseDataHuitDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDTO;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDTORequest;
import com.example.hotrohoctapbackend.DTO.AdminV2.Prediction.StudentExportStudentHuitDetailDTO;
import com.example.hotrohoctapbackend.DTO.LearningPathSuggestionAPI;
import com.example.hotrohoctapbackend.DTO.User.StudentCourseProgressDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.PredictionResultRepository;
import com.example.hotrohoctapbackend.entity.PredictionResult;
import com.example.hotrohoctapbackend.entity.StudentCourseData;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.StudentCourseDataService;
import com.example.hotrohoctapbackend.service.services.ExcelService;
import com.example.hotrohoctapbackend.service.services.PythonScriptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/student-course-data")
public class StudentCourseDataController {

    @Autowired
    private StudentCourseDataService studentCourseDataService;

    @Autowired
    private PythonScriptService studentDataService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private PredictionResultRepository predictionResultRepository;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/predict")
    public ResponseEntity<Map<String, Object>> predictStudentProgress(@RequestBody PredictionRequestDTO predictionRequest) {
        try {
            // Lấy danh sách các đối tượng sinh viên và mã khóa học từ request
            List<StudentDataRequestDTO> studentsData = predictionRequest.getStudentsData();
//            Integer courseId = predictionRequest.getCourseId();
            List<StudentCourseProgressDTO> studentCourseProgressDTOList = new ArrayList<>();
            for (StudentDataRequestDTO studentDataRequestDTO : studentsData) {
                if (studentDataRequestDTO.getAccountId() != null) {
                    StudentCourseProgressDTO item = studentCourseDataService.getCourseProgressFromStudent(studentDataRequestDTO.getStudentId(),
                            studentDataRequestDTO.getAccountId(), predictionRequest.getCourseId());

                    studentCourseProgressDTOList.add(item);
                }

            }

            List<LearningPathSuggestionAPI> studentPredictionDTOList = studentDataService.sendStudentData(studentCourseProgressDTOList);


            // Lưu kết quả dự đoán vào cơ sở dữ liệu (Dựa vào studentId để phân biệt sinh viên)
            for (LearningPathSuggestionAPI prediction : studentPredictionDTOList) {
                // Tìm accountId bằng cách so sánh studentId với studentsData
                String studentId = prediction.getStudent_id();
                Integer accountId = null;

                // Duyệt qua danh sách studentsData để tìm accountId
                for (StudentDataRequestDTO studentData : studentsData) {
                    if (studentData.getStudentId().equals(studentId)) {
                        accountId = studentData.getAccountId();
                        break;  // Dừng vòng lặp khi tìm thấy match
                    }
                }

                if (accountId != null) {
                    // Tạo đối tượng PredictionResult để lưu vào cơ sở dữ liệu
                    PredictionResult predictionResult = new PredictionResult();
                    predictionResult.setStudentId(studentId);

                    predictionResult.setCluster(prediction.getCluster().toString());
                    predictionResult.setClusterDescription(prediction.getCluster_description());
                    predictionResult.setClusterLabel(prediction.getCluster_label());
                    predictionResult.setLearningPathSuggestion(prediction.getLearning_path_suggestion());
                    predictionResult.setPrediction(prediction.getPrediction());
                    predictionResult.setProbability(prediction.getProbability());
                    predictionResult.setRiskLevel(prediction.getRisk_level());
                    predictionResult.setCreatedAt(LocalDateTime.now());
                    predictionResult.setAccount(accountRepository.findById(accountId).get());

                    // Lưu đối tượng PredictionResult vào cơ sở dữ liệu
                    predictionResultRepository.save(predictionResult);
                } else {
                    // Nếu không tìm thấy accountId, có thể cần xử lý lỗi hoặc cảnh báo
                    System.out.println("Không tìm thấy accountId cho studentId: " + studentId);
                }
            }


            // Trả về kết quả thành công
            return ResponseEntity.ok(Map.of("success", true, "message", "Dự đoán và lưu kết quả thành công"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // API để lấy thông tin sinh viên theo accountId và studentId
    @GetMapping("/by-account-and-student/{accountId}/{studentId}")
    public ResponseEntity<StudentCourseData> getStudentCourseDataByAccountIdAndStudentId(
            @PathVariable Integer accountId, @PathVariable String studentId) {
        StudentCourseData data = studentCourseDataService.getStudentCourseDataByAccountIdAndStudentId(accountId, studentId);
        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/list-student-huit/{courseId}")
    public Page<StudentCourseDataDTO> getAllStudentHuitByCourseId(
            @PathVariable Integer courseId,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false, defaultValue = "") String classRoom) {
        return studentCourseDataService.getAllStudentHuitByCourseId(courseId, classRoom, page, size);
    }

    //    Danh sách sinh viên HUIT ở report theo dạng LIST


    @PostMapping("/file-upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Integer courseId) {
        try {
            // Check if file is empty
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected for upload.");
            }

            // Attempt to save the import
            Boolean check = studentCourseDataService.saveImport(file, courseId);

            // Check if the save operation was successful
            if (check) {
                return ResponseEntity.ok("File uploaded successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file data.");
            }

        } catch (Exception e) {
            // Catching any other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }
    }

    // API để lấy tiến trình của học viên
    @GetMapping("/{studentId}/{accountId}/{courseId}")
    public StudentCourseProgressDTO getStudentProgress(
            @PathVariable String studentId,
            @PathVariable Integer accountId,
            @PathVariable Integer courseId) {
        return studentCourseDataService.calculateStudentProgress(studentId, accountId, courseId);
    }

    @GetMapping("/entity/{studentId}/{accountId}/{courseId}")
    public StudentCourseProgressDTO getStudentProgressEntity(
            @PathVariable String studentId,
            @PathVariable Integer accountId,
            @PathVariable Integer courseId) {
        return studentCourseDataService.getCourseProgressFromStudent(studentId, accountId, courseId);
    }

    @PostMapping("/update")
    public Boolean saveStudentProgress(
            @RequestParam String email,
            @RequestParam String studentId,
            @RequestParam Integer accountId,
            @RequestParam Integer courseId) throws JsonProcessingException {

        if (accountId == null) {
            return false;
        }

        return studentCourseDataService.saveProgressDataHUIT(email, studentId, accountId, courseId);
    }

    @GetMapping("/student/{studentId}/account/{accountId}/course/{courseId}")
    public List<StudentCourseProgressDTO> getStudentProgress2(
            @PathVariable String studentId,
            @PathVariable Integer accountId,
            @PathVariable Integer courseId) {

        return studentCourseDataService.getStudentProgress(studentId, accountId, courseId);
    }

    @GetMapping("/dashboard/student-huit/by-course/{courseId}")
    public StudentStatisticsDTO getStatisticsByCourse(@PathVariable Integer courseId,
                                                      @RequestParam(required = false) String classRoom) {
        return studentCourseDataService.getStatisticsByCourse(courseId, classRoom);
    }

    @GetMapping("/students")
    public Page<StudentExportStudentHuitDTO> getStudentsByCourseIdAndResult(
            @RequestParam Integer courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "scoreHigh") String sortBy,
            @RequestParam(required = false) String classRoom

    ) {
        return studentCourseDataService.getAllStudentHuitByCourseIdAndResultPrediction(courseId, page, size, sortBy, classRoom);
    }

    //    Danh sách sinh viên HUIT ở report theo dạng LIST
    @GetMapping("/students-list")
    public List<StudentExportStudentHuitDTO> getStudentsByCourseIdAndResultList(
            @RequestParam Integer courseId
            , @RequestParam(required = false) String classRoom
    ) {
        return studentCourseDataService.getAllStudentHuitByCourseIdAndResultPredictionList(courseId, classRoom);
    }

    @GetMapping("/students/detail")
    public Page<StudentExportStudentHuitDetailDTO> getStudentProgressDetail(
            @RequestParam Integer courseId,
            @RequestParam Integer accountId,
            @RequestParam String studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return studentCourseDataService.getAllStudentHuitDetailByCourseIdAndResultPrediction(courseId, accountId, studentId, page, size);
    }

    @PostMapping("/student-huit/excel/export")
    public ResponseEntity<ApiResponse<byte[]>> exportExcel(@RequestBody StudentExportStudentHuitDTORequest exportRequest) {
        try {
            byte[] excelFile = excelService.exportToExcel(exportRequest.getCourseId(), exportRequest.getClassRooms());
            ApiResponse<byte[]> response = new ApiResponse<>(HttpStatus.OK.value(), "Export Excel thành công!", excelFile);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            ApiResponse<byte[]> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Có lỗi xảy ra khi xuất file Excel!", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}