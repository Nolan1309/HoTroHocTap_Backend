package com.example.hotrohoctapbackend.DTO.AdminV2;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckboxQuestionDTO_V2 {
    private String content; // Nội dung câu hỏi
    private String type; // Loại câu hỏi (checkbox)
    private Integer courseId; // ID khóa học
    private Integer accountId; // ID giáo viên/tài khoản
    private String level; // Mức độ câu hỏi
    private String instruction;
    private ArrayList<OptionDTO> options; // Danh sách các đáp án
    private String topic;
    public CheckboxQuestionDTO_V2() {
    }

    public CheckboxQuestionDTO_V2(String content, String type, Integer courseId, Integer accountId, String level, String instruction, ArrayList<OptionDTO> options, String topic) {
        this.content = content;
        this.type = type;
        this.courseId = courseId;
        this.accountId = accountId;
        this.level = level;
        this.instruction = instruction;
        this.options = options;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    // Getters và Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<OptionDTO> options) {
        this.options = options;
    }


}