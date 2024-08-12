package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "roles")
public interface RoleUserRepository extends JpaRepository<RoleUser,Integer> {
    public RoleUser findByRoleName(String roleName);
}
