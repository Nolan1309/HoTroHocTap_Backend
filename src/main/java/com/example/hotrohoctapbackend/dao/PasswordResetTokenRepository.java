package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PasswordResetToken;
import com.example.hotrohoctapbackend.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(path = "passwordResetToken")
public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken,Integer> {
    PasswordResetToken findByToken(String token);
}
