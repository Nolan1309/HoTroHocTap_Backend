package com.example.hotrohoctapbackend.config;

import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RoleUserRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RoleUser;
import com.example.hotrohoctapbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountService userService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Lấy thông tin người dùng từ Google
        String googleId = oAuth2User.getName(); // ID Google
        String email = oAuth2User.getAttribute("email"); // Email Google
        String name = oAuth2User.getAttribute("name"); // Tên người dùng

        Account account = accountRepository.findByEmailOptional(email).orElse(null);
        if (account != null && account.isDeleted()) {
            throw new OAuth2AuthenticationException("Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ hỗ trợ.");
        }

        RoleUser roleUser = roleUserRepository.findByRoleName("USER");
        account = accountRepository.findByEmailOptional(email).orElseGet(() -> {
            Account newAccount = new Account();
            newAccount.setEmail(email);
            newAccount.setFullname(name);
            newAccount.setGoogleId(googleId);
            newAccount.setRole(roleUser);
            newAccount.setGoogleAccount(true);
            newAccount.setCreatedAt(LocalDateTime.now());
            return accountRepository.save(newAccount);
        });
        // Trả về CustomOAuth2User
        return new CustomOAuth2User(oAuth2User, account);
    }

}
