package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.util.List;

@Data
public class AdminCourseCodeRequestEmailDTO {
    private List<StudentCourseDataDTO> selectedStudents;
    private Integer courseId;
    private Integer accountId;

    public AdminCourseCodeRequestEmailDTO() {
    }

    public AdminCourseCodeRequestEmailDTO(List<StudentCourseDataDTO> selectedStudents, Integer courseId, Integer accountId) {
        this.selectedStudents = selectedStudents;
        this.courseId = courseId;
        this.accountId = accountId;
    }

    public List<StudentCourseDataDTO> getSelectedStudents() {
        return selectedStudents;
    }

    public void setSelectedStudents(List<StudentCourseDataDTO> selectedStudents) {
        this.selectedStudents = selectedStudents;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
