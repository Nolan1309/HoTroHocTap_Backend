package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PredictionResultDTO {
    private String studentId;  // Mã sinh viên
    private Integer accountId;  // Mã sinh viên
    private String cluster;  // Cụm
    private String clusterDescription;  // Mô tả cụm
    private String clusterLabel;  // Nhãn cụm
    private List<String> learningPathSuggestion;  // Gợi ý lộ trình học tập
    private Integer prediction;  // Dự đoán
    private Double probability;  // Xác suất
    private String riskLevel;  // Mức độ rủi ro
    private LocalDateTime createdAt;  // Thời gian tạo
}
