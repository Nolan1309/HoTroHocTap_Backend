package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.VipSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "vipsubscriptions")
public interface VipSubscriptionRepository extends JpaRepository<VipSubscription,Integer> {
}
