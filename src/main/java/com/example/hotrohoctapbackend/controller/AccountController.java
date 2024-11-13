package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.Admin.AddAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.Admin.UpdateAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.PasswordChangeRequest;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAccountAdmin(
            @PathVariable int id,
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") String birthday,
            @RequestParam("roleId") String roleId,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        // Xử lý ảnh (nếu có)
        String base64Image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String data = Base64.getEncoder().encodeToString(imageFile.getBytes());
                base64Image = "data:image/jpeg;base64," + data;

            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Lỗi khi xử lý ảnh");
            }
        }

        // Chuyển đổi birthday từ String sang LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedBirthday = LocalDate.parse(birthday, formatter);

        // Chuyển đổi roleId từ String sang Integer
        Integer parsedRoleId;
        try {
            parsedRoleId = Integer.parseInt(roleId);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("roleId không hợp lệ");
        }

        // Tạo đối tượng DTO với dữ liệu đã chuyển đổi
        UpdateAccountDTOAdmin updateAccountDTOAdmin = new UpdateAccountDTOAdmin();
        updateAccountDTOAdmin.setId(id); // Set id của account
        updateAccountDTOAdmin.setFullname(fullname);
        updateAccountDTOAdmin.setEmail(email);
        updateAccountDTOAdmin.setPhone(phone);
        updateAccountDTOAdmin.setGender(gender);
        updateAccountDTOAdmin.setBirthday(parsedBirthday.atStartOfDay()); // chuyển thành LocalDateTime
        updateAccountDTOAdmin.setRoleId(parsedRoleId);
        updateAccountDTOAdmin.setImage(base64Image);

        // Gọi phương thức update từ service
        return accountService.updateAccountAdmin(updateAccountDTOAdmin);
    }

    @GetMapping("/admin/{id}")
    public UpdateAccountDTOAdmin getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccountAdmin(@PathVariable int id) {
        try {
            Account deletedAccount = accountService.deleteAccountAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<?> activeAccountAdmin(@PathVariable int id) {
        try {
            Account deletedAccount = accountService.activeAccountAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> addAccountAdmin(
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("gender") String gender,
            @RequestParam("password") String password,
            @RequestParam("birthday") String birthday,
            @RequestParam("roleId") String roleId,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedBirthday = LocalDate.parse(birthday, formatter);
            LocalDateTime birthdayDateTime = parsedBirthday.atStartOfDay();

            // Xử lý ảnh (nếu có)
            String base64Image = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                String data = Base64.getEncoder().encodeToString(imageFile.getBytes());
                base64Image = "data:image/jpeg;base64," + data;
            }

            // Tạo đối tượng Account mới và gán các thuộc tính
            AddAccountDTOAdmin newAccount = new AddAccountDTOAdmin();
            newAccount.setFullname(fullname);
            newAccount.setEmail(email);
            newAccount.setPhone(phone);
            newAccount.setGender(gender);
            newAccount.setImage(base64Image);  // Gán ảnh đã mã hóa Base64 vào trường image
            newAccount.setPassword(password);
            newAccount.setBirthday(birthdayDateTime);

            // Chuyển đổi roleId từ String sang Integer
            Integer parsedRoleId;
            try {
                parsedRoleId = Integer.parseInt(roleId);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("roleId không hợp lệ");
            }

            newAccount.setRoleId(parsedRoleId);

            // Lưu tài khoản vào cơ sở dữ liệu
            Account savedAccount = accountService.addAccountAdmin(newAccount);

            return ResponseEntity.ok(savedAccount);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi thêm tài khoản: " + e.getMessage());
        }
    }
}
