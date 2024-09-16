package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AccountDTO;
import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.security.JwtResponse;
import com.example.hotrohoctapbackend.security.LoginRequest;
import com.example.hotrohoctapbackend.service.AccountService;
import com.example.hotrohoctapbackend.service.implement.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/account")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;


    @Autowired
    private JwtService jwtService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/dang-ky")
    public ResponseEntity<?> dandkyTaiKhoan(@RequestBody AccountDTO user){
        ResponseEntity<?> response= accountService.dangkyAccount(user);
        return response;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/dang-nhap")
    public ResponseEntity<?> dangNhap(@RequestBody LoginRequest loginRequest){
        // Xác thực người dùng bằng tên đăng nhập và mật khẩu
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            // Nếu xác thực thành công, tạo token JWT
            if(authentication.isAuthenticated()){
                final String jwt = jwtService.generateToken(loginRequest.getEmail());
                ResponsiveDTOJWT account = accountService.findByAccount(loginRequest.getEmail());
                return ResponseEntity.ok(new JwtResponse(jwt,account));
            }
        }catch (AuthenticationException e){
            // Xác thực không thành công, trả về lỗi hoặc thông báo
            return ResponseEntity.badRequest().body("Tên đăng nhập hặc mật khẩu không chính xác.");
        }
        return ResponseEntity.badRequest().body("Xác thực không thành công.");
    }
}
