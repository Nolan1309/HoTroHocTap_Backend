package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.AccountDTO_Proflie;
import com.example.hotrohoctapbackend.DTO.Admin.AddAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.Admin.UpdateAccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV2.*;
import com.example.hotrohoctapbackend.DTO.AdminV3.Account.AccountDTOAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Account.AccountDTOAdminCreate;
import com.example.hotrohoctapbackend.DTO.AdminV3.AuthorAdmin;
import com.example.hotrohoctapbackend.DTO.AdminV3.Overview;
import com.example.hotrohoctapbackend.DTO.Password.ForgotPasswordRequest;
import com.example.hotrohoctapbackend.DTO.Password.ResetPasswordRequest;
import com.example.hotrohoctapbackend.DTO.PasswordChangeRequest;
import com.example.hotrohoctapbackend.DTO.UpdateAccountDTO;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.PasswordResetTokenRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.PasswordResetToken;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.AccountService;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.example.hotrohoctapbackend.util.MessageTemplate;
import com.example.hotrohoctapbackend.util.TOPIC;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private User_NotificationRepository userNotificationRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private ImageKitService imageKitService;

    @GetMapping("/user/{id}")
    public ApiResponse<AccountDTO> findAccountById(@PathVariable Integer id) {
        AccountDTO accountDTO = accountService.findByAccount(id);
        if (accountDTO == null) {
            return new ApiResponse<>(404, "Account not found", null);
        }
        return new ApiResponse<>(200, "Account found", accountDTO);
    }

    @GetMapping("/profile/{id}")
    public AccountDTO_Proflie findAccountProfileById(@PathVariable Integer id) {
        return accountService.findByAccountProfile(id);
    }

    @GetMapping("/admin/profile/{id}")
    public AccountDTO_Proflie findAccountProfileByIdAdmin(@PathVariable Integer id) {
        return accountService.findByAccountProfile(id);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<AccountDTO_Proflie> updateAccountAdmin(@PathVariable int id, @RequestParam("fullname") String fullname, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("gender") String gender, @RequestParam("birthday") String birthday, // Có thể cần format nếu sử dụng LocalDate

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

    @GetMapping("/list-all-search")
    public ResponseEntity<Page<AccountDetailsDTO_V2>> getPaginatedBlogs(
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,   // Default page is 0
            @RequestParam(defaultValue = "10") int size    // Default page size is 10
    ) {

        Page<AccountDetailsDTO_V2> blogPage = accountService.getAllListAccountSearch(roleId, searchTerm, page, size);

        return ResponseEntity.ok(blogPage);
    }

    // API để cập nhật tài khoản
    @PutMapping("/update/{id}")
    public ApiResponse<AccountDTO_Proflie> updateAccount(@PathVariable int id,
                                                         @RequestParam(value = "fullname", required = false) String fullname,
                                                         @RequestParam(value = "email", required = false) String email,
                                                         @RequestParam(value = "phone", required = false) String phone,
                                                         @RequestParam(value = "gender", required = false) String gender,
                                                         @RequestParam(value = "birthday", required = false) String birthday,
                                                         @RequestPart(value = "image", required = false) MultipartFile imageFile) throws ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException, IOException {

        try {

            ApiResponse<AccountDTO_Proflie> validationResponse = validateAccountInput(fullname, email, phone, gender, birthday);
            if (validationResponse != null) {
                return validationResponse;
            }

            boolean phoneExists = accountService.checkPhoneExists(phone, id);
            if (phoneExists) {
                return new ApiResponse<>(400, "Phone number already exists", null);
            }


            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileExtension = getFileExtension(imageFile.getOriginalFilename());
                if (!isValidImageExtension(fileExtension)) {
                    return new ApiResponse<>(400, "No support extension", null);  // Trả về lỗi nếu định dạng ảnh không hợp lệ
                }
                Result result = imageKitService.uploadFromBytes(imageFile);
                imageUrl = result.getUrl();
            }

            // Cập nhật thông tin người dùng
            UpdateAccountDTO updateAccountDTO = new UpdateAccountDTO();
            updateAccountDTO.setFullname(fullname);
            updateAccountDTO.setEmail(email);
            updateAccountDTO.setPhone(phone);
            updateAccountDTO.setGender(gender);

            // Chuyển đổi ngày sinh từ chuỗi thành LocalDateTime
            try {
                updateAccountDTO.setBirthday(LocalDateTime.parse(birthday));
            } catch (Exception e) {
                return new ApiResponse<>(400, e.getMessage(), null);
            }

            if (imageUrl != null) {
                updateAccountDTO.setImage(imageUrl);
            }

            // Kiểm tra nếu tài khoản không tồn tại và cập nhật thông tin người dùng
            AccountDTO_Proflie updatedAccount = accountService.updateAccountUser(id, updateAccountDTO);
            if (updatedAccount == null) {
                throw new ResourceNotFoundException("Account not found with ID: " + id);  // Ném lỗi nếu không tìm thấy tài khoản
            }

            // Trả về kết quả thành công
            return new ApiResponse<>(200, "Account updated successfully", updatedAccount);

        } catch (ForbiddenException | TooManyRequestsException | InternalServerException | UnauthorizedException |
                 BadRequestException | UnknownException e) {
            return new ApiResponse<>(400, e.getMessage(), null);
        } catch (IOException e) {
            return new ApiResponse<>(500, e.getMessage(), null);
        }
    }

    private ApiResponse<AccountDTO_Proflie> validateAccountInput(String fullname, String email, String phone, String gender, String birthday) {
        if (fullname == null || fullname.trim().isEmpty()) {
            return new ApiResponse<>(400, "Full name cannot be empty", null);
        }
        if (email == null || email.trim().isEmpty()) {
            return new ApiResponse<>(400, "Email cannot be empty", null);
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new ApiResponse<>(400, "Invalid email format", null);
        }
        if (phone == null || phone.trim().isEmpty()) {
            return new ApiResponse<>(400, "Phone number cannot be empty", null);
        }
        if (!phone.matches("^\\+?[0-9]{10,15}$")) {
            return new ApiResponse<>(400, "Invalid phone number format", null);
        }
        if (gender == null || gender.trim().isEmpty()) {
            return new ApiResponse<>(400, "Gender cannot be empty", null);
        }
        if (birthday == null || birthday.trim().isEmpty()) {
            return new ApiResponse<>(400, "Birthday cannot be empty", null);
        }
        try {
            LocalDateTime.parse(birthday);
        } catch (Exception e) {
            return new ApiResponse<>(400, "Invalid birthday format", null);
        }
        return null;
    }

    private boolean isValidImageExtension(String fileExtension) {
        return fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png");
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;  // Mã hóa mật khẩu

    @PutMapping("/change-password/{id}")
    public ApiResponse<String> changePassword(@PathVariable int id, @RequestBody PasswordChangeRequest passwordChangeRequest) {

        Account account = accountService.findAccountByID(id);  // Lấy tài khoản theo ID

        if (account.isGoogleAccount() && account.getPassword() == null) {
            String encodedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
            account.setPassword(encodedNewPassword);
            accountService.updatePassword(account);

            Notification notification = notificationService.getNotificationByTopic(TOPIC.PASSWORD);
            if (notification == null) {
                notification = notificationService.createNotification("ĐỔI MẬT KHẨU", "ĐỔI MẬT KHẨU", TOPIC.PASSWORD);
            }


            UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);

            User_Notification userNotification = new User_Notification();
            userNotification.setAccount(account);
            userNotification.setNotification(notification);
            userNotification.setRead_status(false);
            String registerMessage = MessageTemplate.getMessage(MessageTemplate.Message.REGISTER, account.getFullname());
            userNotification.setMessage(registerMessage);
            userNotificationRepository.save(userNotification);


            messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", notificationDTOUser);
            try {
                emailService.sendNotificationEmail(account.getEmail(), notification.getTitle(), registerMessage);
            } catch (Exception e) {
                System.err.println("Error sending email: " + e.getMessage());
            }
            return new ApiResponse<>(200, "Đổi mật khẩu thành công!", "Success");
        }

        // Kiểm tra mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), account.getPassword())) {
            return new ApiResponse<>(400, "Sai mật khẩu!", "Error");
        }

        // Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            return new ApiResponse<>(400, "Mật khẩu mới và xác nhận mật khẩu không khớp.!", "Error");
        }

        // Cập nhật mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
        account.setPassword(encodedNewPassword);
        accountService.updatePassword(account);  // Cập nhật thông tin tài khoản

        Notification notification = notificationService.getNotificationByTopic(TOPIC.PASSWORD);
        if (notification == null) {
            notification = notificationService.createNotification("ĐỔI MẬT KHẨU", "ĐỔI MẬT KHẨU", TOPIC.PASSWORD);
        }


        UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);

        User_Notification userNotification = new User_Notification();
        userNotification.setAccount(account);
        userNotification.setNotification(notification);
        userNotification.setRead_status(false);
        String registerMessage = MessageTemplate.getMessage(MessageTemplate.Message.REGISTER, account.getFullname());
        userNotification.setMessage(registerMessage);
        userNotificationRepository.save(userNotification);

        messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", notificationDTOUser);
        try {
            emailService.sendNotificationEmail(account.getEmail(), notification.getTitle(), registerMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
        return new ApiResponse<>(200, "Đổi mật khẩu thành công!", "Success");
    }

    @PutMapping("/change-password-admin/{id}")
    public ResponseEntity<String> changePasswordAdmin(@PathVariable int id, @RequestBody PasswordChangeRequest passwordChangeRequest) {

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

        String title = "Thông báo đổi mật khẩu";
        String getMessage = "Tài khoản " + account.getFullname() + " đổi mật khẩu thành công";

        Notification notification = notificationService.createNotification(title, getMessage, TOPIC.PASSWORD);
        UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);
        User_Notification userNotification = new User_Notification();
        userNotification.setAccount(account);
        userNotification.setNotification(notification);
        userNotification.setRead_status(false);
        userNotificationRepository.save(userNotification);


//        emailService.sendNotificationEmail(account.getEmail(), title, getMessage);


        try {
            emailService.sendNotificationEmail(account.getEmail(), title, getMessage);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
//        messagingTemplate.convertAndSend("/topic/" + DoiMatKhau, notification);
        messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", notificationDTOUser);


        return ResponseEntity.ok("Đổi mật khẩu thành công.");
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateAccountAdmin(@PathVariable int id, @RequestParam("fullname") String fullname, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("gender") String gender, @RequestParam("birthday") String birthday, @RequestParam("roleId") String roleId, @RequestPart(value = "image", required = false) MultipartFile imageFile) {

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
    public ResponseEntity<?> addAccountAdmin(@RequestParam("fullname") String fullname, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("gender") String gender, @RequestParam("password") String password, @RequestParam("birthday") String birthday, @RequestParam("roleId") String roleId, @RequestPart(value = "image", required = false) MultipartFile imageFile) {

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        Account user = accountRepository.findByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.badRequest().body("Email not found");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(resetToken);

        emailService.sendResetPasswordEmail(user.getEmail(), token);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        // Tìm token trong MongoDB
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken());

        if (resetToken == null || resetToken.isExpired()) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        // Lấy người dùng từ token
        Account user = resetToken.getAccount();

        // Cập nhật mật khẩu mới cho người dùng (nên dùng BCrypt để mã hóa)
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        accountService.updatePasswordResetUser(user.getId(), user.getPassword());
        // Xóa token sau khi sử dụng
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok("Password has been reset successfully");
    }

    @GetMapping("/list-teacher")
    public List<AdminAccount_V2> getAccountsByRoles() {
        return accountService.getAccountsByRoles();
    }

    @GetMapping("/list-teacher-only")
    public ResponseEntity<List<AccountTeacherDTO_V2>> getActiveAccountsByRole() {
        List<AccountTeacherDTO_V2> accounts = accountService.getActiveAccountsByRole(3);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/restore/list-all-accounts")
    public ResponseEntity<Page<AccountDetailsDTO_V2>> getAllListAccountRestore(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<AccountDetailsDTO_V2> accountDetailsDTOV2s = accountService.getAllListAccountRestore(page, size);
        return ResponseEntity.ok(accountDetailsDTOV2s);
    }

    @GetMapping("/students/list-all-students")
    public Page<AccountDetailsDTO_V2> getLessons(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer roleId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String enrollmentDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (fullName.equals("")) {
            fullName = null;
        }
        if (enrollmentDate.equals("")) {
            enrollmentDate = null;
        }

        return accountService.getAccountStudentByCourseId(courseId, roleId, fullName, enrollmentDate, page, size);
    }

    @GetMapping("/restore/list-all/search-accounts")
    public ResponseEntity<Page<AccountDetailsDTO_V2>> searchAccounts(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String deletedDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AccountDetailsDTO_V2> result = accountService.getAllListAccountRestoreSearch(fullName, deletedDate, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/restore-no-delete/list-all-no-accounts-teacher")
    public List<AccountDetailsDTO_V2> getNoDeletedCourses() {
        return accountService.getAllListAccountAdminAndTeacher();
    }

    @PutMapping("/restore/{accountId}")
    public ResponseEntity<Account> restoreAccount(@PathVariable Integer accountId) {
        AccountDetailsDTO_V2 accountDetails = new AccountDetailsDTO_V2();
        accountDetails.setId(accountId);
        Account restoredAccount = accountService.updateRestoreAccount(accountDetails);
        return ResponseEntity.ok(restoredAccount);
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Integer accountId) {
        AccountDetailsDTO_V2 accountDetails = new AccountDetailsDTO_V2();
        accountDetails.setId(accountId);
        accountService.deleteRestoreAccount(accountDetails);
        return ResponseEntity.ok("Account permanently deleted.");
    }

    @GetMapping("/author")
    public List<AuthorAdmin> getAuthors() {
        return accountService.getAuthorsByRole();
    }

    @GetMapping
    public ApiResponse<Page<AccountDTOAdmin>> getAccounts(
            @RequestParam(value = "fullname", required = false, defaultValue = "") String fullname,
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Account.AccountStatus accountStatus = null;
        if (!status.isEmpty()) {
            try {
                accountStatus = Account.AccountStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Trả về lỗi nếu status không hợp lệ
                return new ApiResponse<>(400, "Invalid status value provided", null);
            }
        }
        Pageable pageable = PageRequest.of(page, size); // Thiết lập phân trang
        Page<AccountDTOAdmin> accountPage = accountService.getAccounts(fullname, accountStatus, pageable);

        return new ApiResponse<>(200, "Accounts fetched successfully", accountPage); // Trả về kết quả
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountDTOAdmin>> createAccount(@RequestBody AccountDTOAdminCreate accountDTO) {
        try {

            boolean EmailCheck = accountService.checkEmailExists(accountDTO.getEmail());
            if (EmailCheck) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Email already", null), HttpStatus.BAD_REQUEST);
            }
            boolean PhoneCheck = accountService.checkPhoneExists(accountDTO.getPhone());
            if (PhoneCheck) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Phone already", null), HttpStatus.BAD_REQUEST);
            }
            // Tạo mới tài khoản từ AccountDTO
            Account account = new Account();
            account.setFullname(accountDTO.getFullname());
            account.setEmail(accountDTO.getEmail());
            account.setPhone(accountDTO.getPhone());

            account.setRole(roleUserRepository.findById(accountDTO.getRoleId()).orElseThrow(() -> new IllegalArgumentException("Role not found")));

            account.setStatus(Account.AccountStatus.valueOf(accountDTO.getStatus().toUpperCase()));
            account.setImage(accountDTO.getImage());
            account.setLastLogin(LocalDateTime.now());
            account.setCreatedAt(LocalDateTime.now());
            account.setUpdatedAt(LocalDateTime.now());

            // Lưu tài khoản vào database
            Account updatedAccount = accountService.createAccount(account, accountDTO.getPassword());


            // Chuyển đổi từ Account entity sang AccountDTO
            AccountDTOAdmin responseDTO = new AccountDTOAdmin(updatedAccount.getId(), updatedAccount.getFullname(),
                    updatedAccount.getEmail(), updatedAccount.getPhone(), updatedAccount.getRole().getRoleName(), updatedAccount.getRole().getId(),
                    updatedAccount.getStatus().toString(), updatedAccount.getEmail(), updatedAccount.getLastLogin(),
                    updatedAccount.getCreatedAt(), updatedAccount.getUpdatedAt());

            // Trả về response
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.value(), "Account created successfully", responseDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountDTOAdmin>> updateAccount(@PathVariable Integer id, @RequestBody AccountDTOAdminCreate accountDTO) {
        try {
            // Tìm tài khoản theo ID
            Account existingAccount = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found"));
            if (!existingAccount.getPhone().equals(accountDTO.getPhone())) {
                boolean PhoneCheck = accountService.checkPhoneExists(accountDTO.getPhone());
                if (PhoneCheck) {
                    return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Phone already", null), HttpStatus.BAD_REQUEST);
                }
            }


            // Cập nhật thông tin tài khoản
            existingAccount.setFullname(accountDTO.getFullname());
            existingAccount.setEmail(accountDTO.getEmail());
            existingAccount.setPhone(accountDTO.getPhone());
            existingAccount.setRole(roleUserRepository.findById(accountDTO.getRoleId()).orElseThrow(() -> new IllegalArgumentException("Role not found")));
            existingAccount.setStatus(Account.AccountStatus.valueOf(accountDTO.getStatus().toUpperCase()));
            existingAccount.setImage(accountDTO.getImage());
            existingAccount.setUpdatedAt(LocalDateTime.now());

            // Lưu tài khoản cập nhật vào database
            Account updatedAccount = accountService.updateAccount(existingAccount, accountDTO.getPassword());

            // Chuyển đổi từ Account entity sang AccountDTO
            AccountDTOAdmin responseDTO = new AccountDTOAdmin(updatedAccount.getId(), updatedAccount.getFullname(),
                    updatedAccount.getEmail(), updatedAccount.getPhone(), updatedAccount.getRole().getRoleName(), updatedAccount.getRole().getId(),
                    updatedAccount.getStatus().toString(), updatedAccount.getEmail(), updatedAccount.getLastLogin(),
                    updatedAccount.getCreatedAt(), updatedAccount.getUpdatedAt());

            // Trả về response
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Account updated successfully", responseDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to update account", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateAccountStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, String> statusRequest) {
        try {
            // Lấy trạng thái từ request body
            String status = statusRequest.get("status");

            // Tìm tài khoản theo ID
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));

            // Cập nhật trạng thái tài khoản
            Account.AccountStatus accountStatus = Account.AccountStatus.valueOf(status.toUpperCase());
            account.setStatus(accountStatus);
            account.setUpdatedAt(LocalDateTime.now());

            // Lưu thay đổi vào database
            accountRepository.save(account);

            // Trả về response thành công
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Account status updated successfully", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Failed to update account status", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/overview/{accountId}")
    public ApiResponse<Overview> getAccountOverview(@PathVariable Integer accountId) {
        try {
            Overview overview = accountService.getOverviewByAccountId(accountId);
            return new ApiResponse<>(200, "Account overview fetched successfully", overview);
        } catch (ResourceNotFoundException e) {
            return new ApiResponse<>(404, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(400, "An error occurred while fetching the account overview", null);
        }
    }

}
