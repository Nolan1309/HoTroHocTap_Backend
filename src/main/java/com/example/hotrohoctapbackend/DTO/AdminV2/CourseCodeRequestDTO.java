package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseCodeRequestDTO {
    private Boolean status;
    private LocalDateTime expiryDate;

    public CourseCodeRequestDTO() {
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CourseCodeRequestDTO(Boolean status, LocalDateTime expiryDate) {
        this.status = status;
        this.expiryDate = expiryDate;
    }
}
