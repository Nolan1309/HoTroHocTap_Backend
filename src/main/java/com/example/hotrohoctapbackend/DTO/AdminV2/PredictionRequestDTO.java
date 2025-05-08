package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.util.List;

@Data
public class PredictionRequestDTO {
    private List<StudentDataRequestDTO> studentsData;
    private Integer courseId;
}
