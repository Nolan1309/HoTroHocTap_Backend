package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RoleUser;
import com.example.hotrohoctapbackend.exception.AccountNotFoundException;
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

    public Account findAccountByID(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Không tìm thấy tài khoản với ID: " + id));
    }

    public Account updatePassword(Account account) {
        return accountRepository.save(account);
    }

    public AccountDTO_Proflie updateAccountUser(int accountId, UpdateAccountDTO updateAccountDTO) {

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            // Cập nhật thông tin tài khoản từ DTO
            account.setFullname(updateAccountDTO.getFullname());
            account.setEmail(updateAccountDTO.getEmail());
            account.setPhone(updateAccountDTO.getPhone());
            account.setGender(updateAccountDTO.getGender());
            account.setBirthday(updateAccountDTO.getBirthday());
            if (updateAccountDTO.getImage() != null) {
                account.setImage(updateAccountDTO.getImage());
            }
            // Cập nhật các trường khác nếu cần

            // Lưu lại thông tin tài khoản vào database
            Account account1 = accountRepository.save(account);

            if (account1 != null) {
                // Tạo đối tượng AccountDTO_Profile để trả về
                AccountDTO_Proflie proflie = new AccountDTO_Proflie();
                proflie.setFullname(account1.getFullname());
                proflie.setEmail(account1.getEmail());
                proflie.setPhone(account1.getPhone());
                proflie.setGender(account1.getGender());
                proflie.setBirthday(account1.getBirthday());
                proflie.setImage(account1.getImage());
                proflie.setId(account1.getId());
                proflie.setCreatedAt(account1.getCreatedAt());
                proflie.setUpdatedAt(account1.getUpdatedAt());

                return proflie;
            } else {
                throw new RuntimeException("Failed to save updated account");
            }
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public AccountDTO_Proflie updateAccountUserNotImage(int accountId, UpdateAccountDTO updateAccountDTO) {

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();

            // Cập nhật thông tin tài khoản từ DTO
            account.setFullname(updateAccountDTO.getFullname());
            account.setEmail(updateAccountDTO.getEmail());
            account.setPhone(updateAccountDTO.getPhone());
            account.setGender(updateAccountDTO.getGender());
            account.setBirthday(updateAccountDTO.getBirthday());

            // Lưu lại thông tin tài khoản vào database
            Account account1 = accountRepository.save(account);

            if (account1 != null) {
                // Tạo đối tượng AccountDTO_Profile để trả về
                AccountDTO_Proflie proflie = new AccountDTO_Proflie();
                proflie.setFullname(account1.getFullname());
                proflie.setEmail(account1.getEmail());
                proflie.setPhone(account1.getPhone());
                proflie.setGender(account1.getGender());
                proflie.setBirthday(account1.getBirthday());
                proflie.setImage(account1.getImage());
                proflie.setId(account1.getId());
                proflie.setCreatedAt(account1.getCreatedAt());
                proflie.setUpdatedAt(account1.getUpdatedAt());

                return proflie;
            } else {
                throw new RuntimeException("Failed to save updated account");
            }
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public AccountDTO_Proflie findByAccountProfile(int id) {
        Optional<Account> account = accountRepository.findById(id);

        AccountDTO_Proflie dto = new AccountDTO_Proflie();
        dto.setId(account.get().getId());
        dto.setImage(account.get().getImage());
        dto.setCreatedAt(account.get().getCreatedAt());
        dto.setUpdatedAt(account.get().getUpdatedAt());
        dto.setRoleId(account.get().getRole().getId());
        dto.setEmail(account.get().getEmail());
        dto.setFullname(account.get().getFullname());
        dto.setPhone(account.get().getPhone());
        dto.setBirthday(account.get().getBirthday());
        dto.setGender(account.get().getGender());

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
