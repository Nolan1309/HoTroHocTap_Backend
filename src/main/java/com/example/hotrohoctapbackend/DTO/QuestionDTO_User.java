package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionDTO_User {
    private int questionId;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private Date createdAt;
    private Date updatedAt;
}
