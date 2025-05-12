package com.example.hotrohoctapbackend.DTO.AdminV2.Prediction;

import lombok.Data;

import java.util.List;

@Data
public class StudentExportStudentHuitDTORequest {
    private Integer courseId;
    private List<String> classRooms;

    public StudentExportStudentHuitDTORequest(Integer courseId, List<String> classRooms) {
        this.courseId = courseId;
        this.classRooms = classRooms;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public List<String> getClassRooms() {
        return classRooms;
    }

    public void setClassRooms(List<String> classRooms) {
        this.classRooms = classRooms;
    }
}
