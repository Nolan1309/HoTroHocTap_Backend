package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentStatisticsDTO {
    private long totalStudents;
    private long passedStudents;
    private long failedStudents;
    private long predictedStudents;
    private long predictedPass;  // Số SV được dự đoán đậu
    private long predictedFail;  // Số SV được dự đoán rớt

    public StudentStatisticsDTO(long passedStudents, long failedStudents, long predictedStudents) {
        this.passedStudents = passedStudents;
        this.failedStudents = failedStudents;
        this.predictedStudents = predictedStudents;
    }
}
