package com.example.hotrohoctapbackend.service.services;

import com.example.hotrohoctapbackend.entity.Account;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface UserService extends UserDetailsService {
    public Account findByEmail(String email);

    Collection<? extends GrantedAuthority> getAuthorities(Account employee);
}
