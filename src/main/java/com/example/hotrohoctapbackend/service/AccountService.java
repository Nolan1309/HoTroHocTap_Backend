package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RoleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    public AccountDTO findByAccount(int id) {
        Optional<Account> account = accountRepository.findById(id);

        AccountDTO dto = new AccountDTO();
        dto.setEmail(account.get().getEmail());
        dto.setFullname(account.get().getFullname());
        dto.setPhone(account.get().getPhone());
        return dto;
    }


    public ResponsiveDTOJWT findByAccount(String email) {
        Account account = accountRepository.findByEmail(email);
        RoleUser role = account.getRole();
        return new ResponsiveDTOJWT(account.getId(), account.getFullname(), account.getEmail(), role.getId());
    }

    public ResponseEntity<Map<String, String>> dangkyAccount(AccountDTO user) {
        if (accountRepository.existsByEmail(user.getEmail())) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Email đã tồn tại!");
            return ResponseEntity.badRequest().body(errorResponse);
        }


        Account user1 = new Account();

        user1.setFullname(user.getFullname());
        user1.setEmail(user.getEmail());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setPhone(user.getPhone());

        user1.setCreatedAt(LocalDateTime.now());
        user1.setUpdatedAt(LocalDateTime.now());

        RoleUser roleUser = new RoleUser();
        roleUser.setId(2);
        user1.setRole(roleUser);

        accountRepository.save(user1);

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Đăng ký thành công!");
        return ResponseEntity.ok(successResponse);
    }
}
