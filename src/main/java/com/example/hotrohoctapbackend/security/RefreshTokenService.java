package com.example.hotrohoctapbackend.security;

import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.RefreshTokenRepository;
import com.example.hotrohoctapbackend.entity.RefreshToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    //    public RefreshToken createRefreshToken(Integer accountId) {
//        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser_Id(accountId);
//
//        RefreshToken refreshToken = new RefreshToken();
//        refreshToken.setUser(accountRepository.findById(accountId).get());
//        refreshToken.setToken(UUID.randomUUID().toString());
//        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
//
//        refreshToken = refreshTokenRepository.save(refreshToken);
//        return refreshToken;
//    }
    public RefreshToken createOrUpdateRefreshToken(Integer accountId) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser_Id(accountId);

        RefreshToken refreshToken;
        if (existingToken.isPresent()) {

            refreshToken = existingToken.get();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(accountRepository.findById(accountId).get());
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        }
        return refreshTokenRepository.save(refreshToken);
    }


    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token đã hết hạn. Vui lòng đăng nhập lại.");
        }
        return token;
    }

    public int deleteByAccountId(Integer accountId) {
        return refreshTokenRepository.deleteByUser(accountRepository.findById(accountId).get());  // Dùng accountId
    }
}
