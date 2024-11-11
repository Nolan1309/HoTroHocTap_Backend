package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.PasswordChangeRequest;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/{id}")
    public AccountDTO findAccountById(@PathVariable Integer id) {
        return accountService.findByAccount(id);
    }

    @GetMapping("/profile/{id}")
    public AccountDTO_Proflie findAccountProfileById(@PathVariable Integer id) {
        return accountService.findByAccountProfile(id);
    }
//    @PutMapping("/profile/{id}")
//    public ResponseEntity<AccountDTO_Proflie> updateAccount(
//            @PathVariable int id,
//            @RequestBody UpdateAccountDTO accountDTO) {
//
//        AccountDTO_Proflie updatedAccount = accountService.updateAccountUser(id, accountDTO);
//        return ResponseEntity.ok(updatedAccount);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AccountDTO_Proflie> updateAccount(
            @PathVariable int id,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") String birthday, // Có thể cần format nếu sử dụng LocalDate
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        // Xử lý ảnh (nếu có)
        String base64Image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String data = Base64.getEncoder().encodeToString(imageFile.getBytes());
                base64Image = "data:image/jpeg;base64," + data;

            } catch (Exception e) {
                return ResponseEntity.badRequest().build(); // Trả về lỗi nếu có vấn đề khi xử lý ảnh
            }
        }

        // Cập nhật thông tin người dùng
        UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
        updateAccountDTO.setFullname(fullname);
        updateAccountDTO.setEmail(email);
        updateAccountDTO.setPhone(phone);
        updateAccountDTO.setGender(gender);
        updateAccountDTO.setBirthday(LocalDateTime.parse(birthday)); // Chuyển birthday về LocalDate
        updateAccountDTO.setImage(base64Image);

        AccountDTO_Proflie updatedAccount = accountService.updateAccountUser(id, updateAccountDTO);
        return ResponseEntity.ok(updatedAccount);
    }


    @Autowired
    private PasswordEncoder passwordEncoder;  // Mã hóa mật khẩu

    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(
            @PathVariable int id,
            @RequestBody PasswordChangeRequest passwordChangeRequest) {

        Account account = accountService.findAccountByID(id);  // Lấy tài khoản theo ID

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), account.getPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu hiện tại không đúng.");
        }

        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        // Cập nhật mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
        account.setPassword(encodedNewPassword);
        accountService.updatePassword(account);  // Cập nhật thông tin tài khoản

        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }
}
