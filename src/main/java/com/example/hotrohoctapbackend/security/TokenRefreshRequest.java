package com.example.hotrohoctapbackend.security;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;

    // Getters and setters
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
