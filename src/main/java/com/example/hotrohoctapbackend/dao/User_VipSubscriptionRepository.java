package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.User_VipSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "user_vipsubscriptions")
public interface User_VipSubscriptionRepository extends JpaRepository<User_VipSubscription,Integer> {
}
