package com.example.hotrohoctapbackend.dao;
import com.example.hotrohoctapbackend.entity.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RepositoryRestResource(path = "verification")
public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Integer> {
    Optional<VerificationRequest> findByEmail(String email);
    Optional<VerificationRequest> findByEmailAndOtpCode(String email, String otpCode);
    void deleteByEmail(String email);
    void deleteById(Long id);
}