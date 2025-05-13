package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.DTO.User.PublicEmail;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.DTO.VerifyRequest;
import com.example.hotrohoctapbackend.config.CustomOAuth2User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.entity.VerificationRequest;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.security.*;
import com.example.hotrohoctapbackend.service.AccountService;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.service.VerificationRequestService;
import com.example.hotrohoctapbackend.service.implement.JwtService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.example.hotrohoctapbackend.util.InputValidator;
import com.example.hotrohoctapbackend.util.MessageTemplate;
import com.example.hotrohoctapbackend.util.TOPIC;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;


@RestController
//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RequestMapping("/account")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private VerificationRequestService verificationRequestService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private User_NotificationRepository userNotificationRepository;

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/dang-ky")
    public ResponseEntity<Map<String, String>> dandkyTaiKhoan(@RequestBody AccountDTO user) {

        ResponseEntity<Map<String, String>> response = accountService.dangkyAccount(user);

        Map<String, String> responseBody = response.getBody();
        if (responseBody != null) {
            String message = responseBody.get("message");

            if (message.equals("Đăng ký thành công!")) {
                String accountID = responseBody.get("accountID");
                String email = responseBody.get("email");
                String title = "Thông báo đăng ký tài khoản";
                String getMessage = "Đăng ký tài khoản thành công ";

                Notification notification = notificationService.createNotification(
                        title, getMessage, TOPIC.REGISTER);
                UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);

                Account account = accountRepository.findById(Integer.parseInt(accountID)).orElseThrow(() -> new RuntimeException("User not found"));

                User_Notification userNotification = new User_Notification();
                userNotification.setAccount(account);
                userNotification.setNotification(notification);
                userNotification.setRead_status(false);
                userNotificationRepository.save(userNotification);

                emailService.sendNotificationEmailDangKy(email, title, getMessage);
//                messagingTemplate.convertAndSend("/topic/" + DangKyTaiKhoan, notification);
                messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", notificationDTOUser);
            }
        }


        return response;
    }

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest) {

        String identifier = loginRequest.getEmail();
        try {
            Authentication authentication;
            String email;
            // Xác thực dựa trên email hoặc số điện thoại
            if (loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()) {
                if (InputValidator.isEmail(identifier)) {
                    email = identifier;
                    authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
                    );
                } else if (InputValidator.isPhone(identifier)) {
                    email = accountRepository.findEmailByPhone(identifier);
                    if (email == null) {
                        return ResponseEntity.badRequest().body("Số điện thoại không tồn tại.");
                    }
                    authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
                    );
                } else {
                    return ResponseEntity.badRequest().body("Định dạng không hợp lệ. Vui lòng nhập email hoặc số điện thoại.");
                }
            } else {
                return ResponseEntity.badRequest().body("Vui lòng cung cấp email hoặc số điện thoại.");
            }

            // Nếu xác thực thành công
            if (authentication.isAuthenticated()) {
                String jwt = jwtService.generateToken(email);
                ResponsiveDTOJWT account = accountService.findByAccount(email);

                Account account1 = accountRepository.findByEmail(email);
                if (account1 != null && account1.isDeleted()) {
                    return ResponseEntity.badRequest().body("Tài khoản đã bị khóa.");
                }

                String refreshToken = refreshTokenService.createOrUpdateRefreshToken(account.getId()).getToken();

                return ResponseEntity.ok(new JwtResponse(jwt, account, refreshToken));
            }
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }
        return ResponseEntity.badRequest().body("Xác thực không thành công.");
    }


//    @CrossOrigin(origins = "http://localhost:3000", )

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @GetMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam("refreshToken") String requestRefreshToken) {
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    String newAccessToken = jwtService.generateToken(refreshToken.getUser().getEmail());
                    ResponsiveDTOJWT account = accountService.findByAccount(refreshToken.getUser().getEmail());
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, account, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token không hợp lệ hoặc đã hết hạn."));
    }


    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @GetMapping("/oauth2/success")
    public void googleLogin(Authentication authentication, HttpServletResponse response) throws IOException {
        try {
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không thể xác thực người dùng.");
                return;
            }

            CustomOAuth2User customUser = (CustomOAuth2User) authentication.getPrincipal();
            Account account = customUser.getAccount();

            if (account.isDeleted()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tài khoản đã bị khóa.");
                return;
            }

            Notification notification = notificationService.createNotification(
                    "Đăng ký tài khoản", "Tài khoản " + account.getFullname() + " đăng ký thành công !", TOPIC.REGISTER);

//            Optional<Account> account = accountRepository.findById(request.getUserId().intValue());

            UserNotificationDTO_User user = new UserNotificationDTO_User(notification, false);

            User_Notification userNotification = new User_Notification();
            userNotification.setAccount(account);
            userNotification.setNotification(notification);
            userNotification.setRead_status(false);
            userNotificationRepository.save(userNotification);


            messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", user);
            // Tạo JWT và Refresh Token
            final String jwt = jwtService.generateToken(account.getEmail());
            String refreshToken = refreshTokenService.createOrUpdateRefreshToken(account.getId()).getToken();
            ResponsiveDTOJWT accountRT = accountService.findByAccount(account.getEmail());


            Cookie jwtCookie = new Cookie("authToken", URLEncoder.encode(new ObjectMapper().writeValueAsString(jwt), "UTF-8"));
            jwtCookie.setHttpOnly(false);
            jwtCookie.setSecure(false); // Chỉ bật `true` nếu dùng HTTPS
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60); // 1 giờ
            response.addCookie(jwtCookie);

            // Lưu Refresh Token vào HttpOnly Cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", URLEncoder.encode(new ObjectMapper().writeValueAsString(refreshToken), "UTF-8"));
            refreshTokenCookie.setHttpOnly(false);
            refreshTokenCookie.setSecure(false); // Chỉ bật `true` nếu dùng HTTPS
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            response.addCookie(refreshTokenCookie);

            // Lưu thông tin ResponsiveDTOJWT vào Cookie (nếu cần)
            Cookie userInfoCookie = new Cookie("userInfo", URLEncoder.encode(new ObjectMapper().writeValueAsString(accountRT), "UTF-8"));
            userInfoCookie.setHttpOnly(false); // Cho phép đọc bởi JavaScript nếu cần
            userInfoCookie.setSecure(false);
            userInfoCookie.setPath("/");
            userInfoCookie.setMaxAge(60 * 60); // 1 giờ
            response.addCookie(userInfoCookie);

            // Chuyển hướng về giao diện
            response.sendRedirect("http://localhost:3000");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đăng nhập Google thất bại: " + e.getMessage());
        }
    }

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/register-generate")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody AccountDTO request) {

        if (accountService.checkEmailExists(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Email đã tồn tại!", System.currentTimeMillis()));
        }
        if (accountService.checkPhoneExists(request.getPhone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, "Phone đã tồn tại!", System.currentTimeMillis()));
        }

        verificationRequestService.createVerificationRequest(
                request.getFullname(),
                request.getEmail(),
                request.getPassword(),
                request.getBirthday(),
                request.getPhone()
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(200, "Mã OTP đã được gửi tới email của bạn!", null));
    }

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/register-generate-sms")
    public ResponseEntity<String> registerSMS(@RequestBody AccountDTO request) {
        String otp = verificationRequestService.createVerificationRequestSMS(
                request.getFullname(),
                request.getEmail(),
                request.getPassword(),
                request.getBirthday(),
                request.getPhone()
        );
        return ResponseEntity.ok(otp);
    }

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody VerifyRequest request) {
        if ("REGISTER".equals(request.getType())) {
            boolean isVerified = verificationRequestService.verifyOTP(request.getEmail(), request.getOtp());

            if (isVerified) {
                Optional<VerificationRequest> requestOptional = verificationRequestService.getVerificationRequestByEmail(request.getEmail());
                if (requestOptional.isPresent()) {
                    Account account = accountService.saveTaiKhoan(requestOptional.get().getFullname(), requestOptional.get().getPassword(), requestOptional.get().getPhone(),
                            requestOptional.get().getBirthday(), requestOptional.get().getEmail());

                    verificationRequestService.deleteVerificationRequestByEmail((request.getEmail()));

                    Notification notification = notificationService.getNotificationByTopic(TOPIC.REGISTER);
                    if (notification == null) {
                        notification = notificationService.createNotification("ĐĂNG KÝ TÀI KHOẢN", "ĐĂNG KÝ TÀI KHOẢN", TOPIC.REGISTER);
                    }
                    UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);

                    User_Notification userNotification = new User_Notification();
                    userNotification.setAccount(account);
                    userNotification.setNotification(notification);
                    userNotification.setTopic(TOPIC.REGISTER);
                    userNotification.setCreatedAt(LocalDateTime.now());
                    userNotification.setRead_status(false);

                    String registerMessage = MessageTemplate.getMessage(MessageTemplate.Message.REGISTER, account.getFullname());
                    userNotification.setMessage(registerMessage);
                    userNotificationRepository.save(userNotification);

                    emailService.sendNotificationEmailDangKy(account.getEmail(), notification.getTitle(), registerMessage);

                    messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()), "/queue/notifications", notificationDTOUser);
                    ApiResponse<String> response = new ApiResponse<>(200, "Xác thực thành công!", "Success");
                    return ResponseEntity.ok(response);
                } else {
                    // Nếu không tìm thấy yêu cầu xác thực
                    ApiResponse<String> response = new ApiResponse<>(400, "Không tìm thấy yêu cầu xác thực.", "Failure");
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                // Nếu OTP không chính xác
                ApiResponse<String> response = new ApiResponse<>(400, "OTP không chính xác hoặc đã hết hạn.", "Failure");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            ApiResponse<String> response = new ApiResponse<>(200, "Xác thực thành công!", "Success");
            return ResponseEntity.ok(response);
        }
    }

    //    @CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
    @PostMapping("/email-public")
    public ResponseEntity<ApiResponse<?>> getEmailAndCheck(@RequestBody PublicEmail email) {
        try {
            boolean existEmail = accountService.checkExistEmail(email.getEmail());

            if (existEmail) {
                ResponsiveDTOJWT account = accountService.findByAccount(email.getEmail());
                ApiResponse<ResponsiveDTOJWT> response = new ApiResponse<>(200, "Email found.", account);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<String> response = new ApiResponse<>(400, "Email does not exist.", null);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<String> response = new ApiResponse<>(500, "An error occurred while processing the request.", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
