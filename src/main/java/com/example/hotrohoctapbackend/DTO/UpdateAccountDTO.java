package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAccountDTO {
    private String fullname;
    private String email;
    private String phone;
    private String gender;
    private String image;
    private LocalDateTime birthday;
}