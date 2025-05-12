package com.example.hotrohoctapbackend.service.implement;


import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RoleUser;
import com.example.hotrohoctapbackend.service.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtService {
    //    public static final String SERECT = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    @Value("${app.jwtSecret}")
    private String SERECT;
    @Autowired
    private UserService userService;

    // Tạo JWT dựa trên tên đang nhập
    public String generateToken(String tenDangNhap) {
        Map<String, Object> claims = new HashMap<>();

        boolean isAdmin = false;
        boolean isTeacher = false;
        boolean isUser = false;
        boolean isUserVip = false;

        boolean isHuitStudent = false;
        Account account = userService.findByEmail(tenDangNhap);
        if (account != null && account.getAuthorities().size() > 0) {
            RoleUser role = account.getRole();
            if (role != null) {
                if (role.getRoleName().equals("ADMIN")) {
                    isAdmin = true;
                }
                if (role.getRoleName().equals("TEACHER")) {
                    isTeacher = true;
                }
                if (role.getRoleName().equals("USER")) {
                    isUser = true;
                }
                if (role.getRoleName().equals("USERVIP")) {
                    isUserVip = true;
                }
                if (role.getRoleName().equals("HUITSTUDENT")) {
                    isHuitStudent = true;
                }
            }
        }
        claims.put("isAdmin", isAdmin);
        claims.put("isTeacher", isTeacher);
        claims.put("isUser", isUser);
        claims.put("isUserVip", isUserVip);
        claims.put("isHuitStudent", isHuitStudent);

//        claims.put("isAdmin", true);
//        claims.put("x", "ABC");
        return createToken(claims, tenDangNhap);
    }

    // Tạo JWT với các claim đã chọn
    private String createToken(Map<String, Object> claims, String tenDangNhap) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(tenDangNhap)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6 * 60 * 60 * 1000)) // JWT hết hạn sau 30 phút
                .signWith(SignatureAlgorithm.HS256, getSigneKey())
                .compact();
    }

    // Lấy serect key
    private Key getSigneKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SERECT);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigneKey()).parseClaimsJws(token).getBody();
    }

    // Trích xuất thông tin cho 1 claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Kiểm tra tời gian hết hạn từ JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Kiểm tra cái JWT đã hết hạn
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Kiểm tra tính hợp lệ
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String tenDangNhap = extractUsername(token);
        System.out.println(tenDangNhap);
        return (tenDangNhap.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}