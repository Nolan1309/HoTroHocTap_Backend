package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Subscriptions_Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscription_account")
public interface SubscriptionAccountRepository extends JpaRepository<Subscriptions_Account, Integer> {
}
