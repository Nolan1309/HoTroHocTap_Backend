package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends JpaRepository<Account,Integer> {
    public boolean existsByEmail(String email);
    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Optional<Account> findByEmailOptional(@Param("email") String email);

    public Account findByEmail(String email);
    public Account findByGoogleId(String googleId);
}
