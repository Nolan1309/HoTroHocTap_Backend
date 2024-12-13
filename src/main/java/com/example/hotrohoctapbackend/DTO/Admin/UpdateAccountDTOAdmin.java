package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UpdateAccountDTOAdmin {
    private Integer id;
    private String fullname;
    private String email;
    private String phone;
    private String gender;
    private String image;
    private LocalDateTime birthday;
    private Integer roleId;
}
