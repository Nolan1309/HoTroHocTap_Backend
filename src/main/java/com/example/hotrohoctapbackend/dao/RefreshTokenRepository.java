package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "refresh_tokens")
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(Account user);
    Optional<RefreshToken> findByUser_Id(Integer accountId);
}
