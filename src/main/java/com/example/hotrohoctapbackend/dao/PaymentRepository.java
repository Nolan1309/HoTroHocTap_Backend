package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "payments")
public interface PaymentRepository extends JpaRepository<Payment,Integer> {
}
