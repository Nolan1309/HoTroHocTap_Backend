package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

@Data
public class CourseCodeStatusResponse {
    private boolean isValid;
    private String code;

    public CourseCodeStatusResponse(boolean isValid, String code) {
        this.isValid = isValid;
        this.code = code;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
