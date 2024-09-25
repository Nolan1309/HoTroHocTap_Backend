package com.example.hotrohoctapbackend.security;

import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;

public class JwtResponse {
    private final String jwt;
    private String refreshToken;
    private ResponsiveDTOJWT responsiveDTOJWT;

    public JwtResponse(String jwt, ResponsiveDTOJWT responsiveDTOJWT,String refreshToken) {
        this.jwt = jwt;
        this.responsiveDTOJWT = responsiveDTOJWT;
        this.refreshToken = refreshToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getJwt() {
        return jwt;
    }

    public ResponsiveDTOJWT getResponsiveDTOJWT() {
        return responsiveDTOJWT;
    }

}