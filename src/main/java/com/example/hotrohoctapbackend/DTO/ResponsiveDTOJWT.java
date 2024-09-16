package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ResponsiveDTOJWT {

    private int id;
    private String fullname;
    private String email;
    private int roleId;

    public ResponsiveDTOJWT() {
    }

    public ResponsiveDTOJWT(int id, String fullname, String email, int roleId) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.roleId = roleId;
    }
}
