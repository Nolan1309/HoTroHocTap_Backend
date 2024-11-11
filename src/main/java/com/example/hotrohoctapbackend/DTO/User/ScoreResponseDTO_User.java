package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

@Data
public class ScoreResponseDTO_User {
    private int correct;
    private int uncorrect;
    private int total;
    private double score;
}
