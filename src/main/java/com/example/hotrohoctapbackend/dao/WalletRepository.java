package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "wallet")
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
}
