package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.util.List;

@Data
public class AdminQuestionExamCreate {
    private AdminTestAddDTO_V2 adminTestAddDTOV2;
    private List<String> types;
    private Integer estimateTest;

    public AdminQuestionExamCreate() {
    }

    public AdminQuestionExamCreate(AdminTestAddDTO_V2 adminTestAddDTOV2, List<String> types, Integer estimateTest) {
        this.adminTestAddDTOV2 = adminTestAddDTOV2;
        this.types = types;
        this.estimateTest = estimateTest;
    }

    public AdminTestAddDTO_V2 getAdminTestAddDTOV2() {
        return adminTestAddDTOV2;
    }

    public void setAdminTestAddDTOV2(AdminTestAddDTO_V2 adminTestAddDTOV2) {
        this.adminTestAddDTOV2 = adminTestAddDTOV2;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getEstimateTest() {
        return estimateTest;
    }

    public void setEstimateTest(Integer estimateTest) {
        this.estimateTest = estimateTest;
    }
}
