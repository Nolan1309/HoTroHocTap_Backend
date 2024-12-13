package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettingDTO {
    private String name;
    private String type;
    private boolean isCheck;

}
