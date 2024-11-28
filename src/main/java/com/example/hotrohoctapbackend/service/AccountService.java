package com.example.hotrohoctapbackend.service;


import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.Admin.AddAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.Admin.UpdateAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RoleUser;
import com.example.hotrohoctapbackend.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        Account account = accountRepository.save(user1);
        if (account != null) {
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Đăng ký thành công!");
            successResponse.put("accountID", String.valueOf(account.getId()));
            successResponse.put("email", account.getEmail());
            successResponse.put("fullname", account.getFullname());
            successResponse.put("phone", account.getPhone());
            return ResponseEntity.ok(successResponse);
        } else {
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Đăng ký không thành công!");
            return ResponseEntity.ok(successResponse);
        }
    }

    public UpdateAccountDTOAdmin getAccountById(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        return convertToDTOAdmin(account);
    }

    public ResponseEntity<?> updateAccountAdmin(UpdateAccountDTOAdmin updatedAccountDTO) {
        Optional<Account> existingAccountOpt = accountRepository.findById(updatedAccountDTO.getId());

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();
            existingAccount.setFullname(updatedAccountDTO.getFullname());
            existingAccount.setEmail(updatedAccountDTO.getEmail());
            existingAccount.setPhone(updatedAccountDTO.getPhone());
            existingAccount.setGender(updatedAccountDTO.getGender());

            if (updatedAccountDTO.getImage() != null) {
                existingAccount.setImage(updatedAccountDTO.getImage());
            } else {
                existingAccount.setImage(existingAccountOpt.get().getImage());
            }


            existingAccount.setBirthday(updatedAccountDTO.getBirthday());
            existingAccount.setUpdatedAt(LocalDateTime.now());

            // Kiểm tra và cập nhật role nếu có
            if (updatedAccountDTO.getRoleId() != null) {
                RoleUser role = roleUserRepository.findById(updatedAccountDTO.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Role với id: " + updatedAccountDTO.getRoleId()));
                existingAccount.setRole(role);
            }

            Account updatedAccount = accountRepository.save(existingAccount);
            return ResponseEntity.ok(updatedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy Account với id: " + updatedAccountDTO.getId());
        }
    }

    public Account deleteAccountAdmin(int accountId) {
        // Tìm tài khoản theo ID
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public Account activeAccountAdmin(int accountId) {
        // Tìm tài khoản theo ID
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + accountId);
        }
    }

    public Account addAccountAdmin(AddAccountDTOAdmin accountDTOAdmin) {
        // Tạo đối tượng Account mới
        Account newAccount = new Account();

        // Đặt các thuộc tính cho Account từ DTO
        newAccount.setFullname(accountDTOAdmin.getFullname());
        newAccount.setEmail(accountDTOAdmin.getEmail());
        newAccount.setPhone(accountDTOAdmin.getPhone());
        newAccount.setGender(accountDTOAdmin.getGender());
        newAccount.setBirthday(accountDTOAdmin.getBirthday());
        newAccount.setImage(accountDTOAdmin.getImage());

        // Mặc định ban đầu cho trường createdAt và updatedAt là thời điểm hiện tại
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setUpdatedAt(LocalDateTime.now());

        // Thiết lập role cho tài khoản mới
        if (accountDTOAdmin.getRoleId() != null) {
            RoleUser role = roleUserRepository.findById(accountDTOAdmin.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + accountDTOAdmin.getRoleId()));
            newAccount.setRole(role);
        }

        // Thiết lập trạng thái ban đầu cho isDeleted và deletedDate
        newAccount.setDeleted(false);
        newAccount.setDeletedDate(LocalDateTime.now());

        // Mã hóa mật khẩu
        String encodedPassword = passwordEncoder.encode(accountDTOAdmin.getPassword());
        newAccount.setPassword(encodedPassword);

        // Lưu đối tượng Account mới vào cơ sở dữ liệu
        return accountRepository.save(newAccount);
    }

    private UpdateAccountDTOAdmin convertToDTOAdmin(Account account) {
        UpdateAccountDTOAdmin dto = new UpdateAccountDTOAdmin();
        dto.setId(account.getId());
        dto.setFullname(account.getFullname());
        dto.setEmail(account.getEmail());
        dto.setPhone(account.getPhone());
        dto.setGender(account.getGender());
        dto.setImage(account.getImage());
        dto.setBirthday(account.getBirthday());
        dto.setRoleId(account.getRole().getId());
        return dto;
    }

    public Account updatePasswordResetUser(Integer accountId, String passwordReset) {
        Optional<Account> userOptional = accountRepository.findById(accountId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " not found");
        }
        Account user = userOptional.get();
        user.setPassword(passwordReset);
        return accountRepository.saveAndFlush(user);
    }

}
