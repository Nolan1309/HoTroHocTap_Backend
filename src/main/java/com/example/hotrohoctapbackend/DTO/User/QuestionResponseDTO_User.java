package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class QuestionResponseDTO_User {
    private Integer id;
    private String instruction;
    private String correct_show;
    private String correct_check;
}
