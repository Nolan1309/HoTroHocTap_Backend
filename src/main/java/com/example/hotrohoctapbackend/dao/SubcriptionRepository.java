package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "subscription")
public interface SubcriptionRepository extends JpaRepository<Subscription, Integer> {
}
