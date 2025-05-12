package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseCodeResponseDTO {
    private Integer id;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
    private boolean status;
    private LocalDateTime expiryDate;
    private Integer accountId;
    private Integer courseId;


    public CourseCodeResponseDTO(Integer id, String code, LocalDateTime createdAt, LocalDateTime usedAt, boolean status, LocalDateTime expiryDate, Integer accountId, Integer courseId) {
        this.id = id;
        this.code = code;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
        this.status = status;
        this.expiryDate = expiryDate;
        this.accountId = accountId;
        this.courseId = courseId;
    }

//    public CourseCodeResponseDTO(Integer id, String code, boolean status, LocalDateTime expiryDate, LocalDateTime createdAt) {
//        this.id = id;
//        this.code = code;
//        this.status = status;
//        this.expiryDate = expiryDate;
//        this.createdAt = createdAt;
//    }

    public CourseCodeResponseDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
