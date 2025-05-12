package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class QuestionUserExamPayloadDTOItem {
    private Integer index;
    private String answer;

    public QuestionUserExamPayloadDTOItem(Integer index, String answer) {
        this.index = index;
        this.answer = answer;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
