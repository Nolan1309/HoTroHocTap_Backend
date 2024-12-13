package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.DTO.User.UserNotificationDTO_User;
import com.example.hotrohoctapbackend.config.CustomOAuth2User;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.User_NotificationRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.Notification;
import com.example.hotrohoctapbackend.entity.User_Notification;
import com.example.hotrohoctapbackend.security.*;
import com.example.hotrohoctapbackend.service.AccountService;
import com.example.hotrohoctapbackend.service.NotificationService;
import com.example.hotrohoctapbackend.service.implement.JwtService;
import com.example.hotrohoctapbackend.service.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;


import static com.example.hotrohoctapbackend.util.topic.DangKyTaiKhoan;


@RestController
//@CrossOrigin(origins = "http://localhost:3000")
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
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private User_NotificationRepository userNotificationRepository;

    @CrossOrigin(origins = "http://localhost:3000")
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
                        title, getMessage, DangKyTaiKhoan);
                UserNotificationDTO_User notificationDTOUser = new UserNotificationDTO_User(notification, false);

                Account account = accountRepository.findById(Integer.parseInt(accountID)).orElseThrow(() -> new RuntimeException("User not found"));
                User_Notification userNotification = new User_Notification();
                userNotification.setAccount(account);
                userNotification.setNotification(notification);
                userNotification.setRead_status(false);
                userNotificationRepository.save(userNotification);

//                emailService.sendNotificationEmailDangKy(email, title, getMessage);
//                messagingTemplate.convertAndSend("/topic/" + DangKyTaiKhoan, notification);
                messagingTemplate.convertAndSendToUser(String.valueOf(account.getId()),  "/queue/notifications", notificationDTOUser);
            }
        }


        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest) {
        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            // Nếu xác thực thành công, tạo token JWT
            if (authentication.isAuthenticated()) {


                final String jwt = jwtService.generateToken(loginRequest.getEmail());
                ResponsiveDTOJWT account = accountService.findByAccount(loginRequest.getEmail());

                Account account1 = accountRepository.findByEmail(loginRequest.getEmail());
                if (account1 != null && account1.isDeleted()) {
                    return ResponseEntity.badRequest().body("Tài khoản đã bị khóa.");
                }
                String refreshToken = refreshTokenService.createOrUpdateRefreshToken(account.getId()).getToken();


                return ResponseEntity.ok(new JwtResponse(jwt, account, refreshToken));
            }
        } catch (AuthenticationException e) {
            // Xác thực không thành công, trả về lỗi hoặc thông báo
            return ResponseEntity.badRequest().body("Tên đăng nhập hặc mật khẩu không chính xác.");
        }
        return ResponseEntity.badRequest().body("Xác thực không thành công.");
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
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


    @CrossOrigin(origins = "http://localhost:3000")
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
                    "Đăng ký tài khoản", "Tài khoản "+account.getFullname()+ " đăng ký thành công !", DangKyTaiKhoan);

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


}
