package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.util.Date;

@Data
public class UserQuestionExamDTO_Checkbox {

    private int questionId;

    private String content;

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;


    private Date createdAt;
    private Date updatedAt;

}
