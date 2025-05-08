package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminQuestionGetDTO_V2 {
    private int id;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String result;
    private String instruction;
    private String resultCheck;
    private String level;
    private String type;
    private Integer accountId;
    private Integer courseId;
    private String topic;
    private LocalDateTime createdAt;

    public AdminQuestionGetDTO_V2(int id, String content, String optionA, String optionB, String optionC, String optionD, String result, String instruction, String resultCheck, String level, String type, Integer accountId, Integer courseId, String topic
            , LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.result = result;
        this.instruction = instruction;
        this.resultCheck = resultCheck;
        this.level = level;
        this.type = type;
        this.accountId = accountId;
        this.courseId = courseId;
        this.topic = topic;
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public AdminQuestionGetDTO_V2() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getResultCheck() {
        return resultCheck;
    }

    public void setResultCheck(String resultCheck) {
        this.resultCheck = resultCheck;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
}
