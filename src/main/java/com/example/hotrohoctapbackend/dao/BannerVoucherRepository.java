package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.BannerVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "bannerVoucher")
public interface BannerVoucherRepository extends JpaRepository<BannerVoucher, Integer> {
}
