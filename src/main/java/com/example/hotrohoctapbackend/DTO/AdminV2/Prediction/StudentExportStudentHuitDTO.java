package com.example.hotrohoctapbackend.DTO.AdminV2.Prediction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentExportStudentHuitDTO {
    private String studentId;
    private String accountId;
    private String fullname;
    private String classRoom;
    private int age;
    private String gender;
    private double assignmentCompletionRate;
    //    private Integer chaptersLearned;
//    private Integer chaptersCompleted;
    private double examScore; //Điểm trung bình
    private double probability; //Tỉ lệ đậu rớt
    private String prediction; //Kết quả đậu hay rớt

    public StudentExportStudentHuitDTO() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getAssignmentCompletionRate() {
        return assignmentCompletionRate;
    }

    public void setAssignmentCompletionRate(double assignmentCompletionRate) {
        this.assignmentCompletionRate = assignmentCompletionRate;
    }

//    public Integer getChaptersLearned() {
//        return chaptersLearned;
//    }
//
//    public void setChaptersLearned(Integer chaptersLearned) {
//        this.chaptersLearned = chaptersLearned;
//    }
//
//    public Integer getChaptersCompleted() {
//        return chaptersCompleted;
//    }
//
//    public void setChaptersCompleted(Integer chaptersCompleted) {
//        this.chaptersCompleted = chaptersCompleted;
//    }

    public double getExamScore() {
        return examScore;
    }

    public void setExamScore(double examScore) {
        this.examScore = examScore;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
}
