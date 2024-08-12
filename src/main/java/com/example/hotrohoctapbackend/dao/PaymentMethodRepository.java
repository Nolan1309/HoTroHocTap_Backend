package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "paymentmethod")
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Integer> {
}
