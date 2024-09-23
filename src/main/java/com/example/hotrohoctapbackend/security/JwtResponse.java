package com.example.hotrohoctapbackend.security;

import com.example.hotrohoctapbackend.DTO.ResponsiveDTOJWT;

public class JwtResponse {
    private final String jwt;
    private ResponsiveDTOJWT responsiveDTOJWT;

    public JwtResponse(String jwt, ResponsiveDTOJWT responsiveDTOJWT) {
        this.jwt = jwt;
        this.responsiveDTOJWT = responsiveDTOJWT;
    }

    public String getJwt() {
        return jwt;
    }

    public ResponsiveDTOJWT getResponsiveDTOJWT() {
        return responsiveDTOJWT;
    }

}