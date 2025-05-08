package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.util.List;
@Data
public class CopyQuestionsRequest {
    private List<Integer> questionIds;
    private Integer targetCourseId;
}
