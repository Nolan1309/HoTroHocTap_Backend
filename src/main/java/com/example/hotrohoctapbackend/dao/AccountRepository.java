package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends JpaRepository<Account,Integer> {
    public boolean existsByEmail(String email);

    public Account findByEmail(String email);

}
