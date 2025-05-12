package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class CourseCodeActivationRequest {
    private String code;
    private Integer accountId;

    public CourseCodeActivationRequest(String code, Integer accountId) {
        this.code = code;
        this.accountId = accountId;
    }

    public CourseCodeActivationRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
