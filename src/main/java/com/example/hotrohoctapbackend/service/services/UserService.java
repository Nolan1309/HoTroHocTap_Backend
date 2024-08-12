package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.entity.Account;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public Account findByEmail(String email);
}
