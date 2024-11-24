package com.example.hotrohoctapbackend.DTO.Password;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String token;
    private String password;
}
