package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "verification_request")
public class VerificationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "fullname")
    private String fullname;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private String phone;
    @Column(name = "birthday")
    private LocalDateTime birthday;
    @Column(name = "password")
    private String password;
    @Column(name = "otpCode")
    private String otpCode;
    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;
}
