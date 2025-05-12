package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.List;

@Data
public class QuestionUserExamPayloadDTO {

    private List<QuestionUserExamPayloadDTOItem> answer;
    private String type;
    private String questionId;

    public QuestionUserExamPayloadDTO(List<QuestionUserExamPayloadDTOItem> answer, String type, String questionId) {
        this.answer = answer;
        this.type = type;
        this.questionId = questionId;
    }

    public List<QuestionUserExamPayloadDTOItem> getAnswer() {
        return answer;
    }

    public void setAnswer(List<QuestionUserExamPayloadDTOItem> answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }


}
