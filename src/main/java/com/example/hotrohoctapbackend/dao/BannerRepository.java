package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "banners")
public interface BannerRepository extends JpaRepository<Banner,Integer> {
}
