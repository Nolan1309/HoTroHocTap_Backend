package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class AddAccountDTOAdmin {
    private Integer id;
    private String fullname;
    private String email;
    private String phone;
    private String gender;
    private String image;
    private String password;
    private LocalDateTime birthday;
    private Integer roleId;
}
