package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
public class UserQuestionExamDTO {
    @Getter
    private int id;
    @Getter
    private String content;
    @Getter
    private LocalDateTime createdAt;
    @Getter
    private LocalDateTime deletedDate;
    @Getter
    private String instruction;
    private boolean isDeleted;
    @Getter
    private String level;
    @Getter
    private String optionA;
    @Getter
    private String optionB;
    @Getter
    private String optionC;
    @Getter
    private String optionD;
    @Getter
    private String result;
    @Getter
    private String resultCheck;
    @Getter
    private String topic;
    @Getter
    private String type;
    @Getter
    private LocalDateTime updatedAt;
    @Getter
    private int accountId;
    @Getter
    private int courseId;

    public UserQuestionExamDTO() {
    }

    public UserQuestionExamDTO(int id, String content, LocalDateTime createdAt, LocalDateTime deletedDate, String instruction, boolean isDeleted, String level, String optionA, String optionB, String optionC, String optionD, String result, String resultCheck, String topic, String type, LocalDateTime updatedAt, int accountId, int courseId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.deletedDate = deletedDate;
        this.instruction = instruction;
        this.isDeleted = isDeleted;
        this.level = level;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.result = result;
        this.resultCheck = resultCheck;
        this.topic = topic;
        this.type = type;
        this.updatedAt = updatedAt;
        this.accountId = accountId;
        this.courseId = courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeletedDate(LocalDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setResultCheck(String resultCheck) {
        this.resultCheck = resultCheck;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
