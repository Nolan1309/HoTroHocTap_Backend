package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Banner.BannerVoucherDTO;
import com.example.hotrohoctapbackend.dao.BannerVoucherRepository;
import com.example.hotrohoctapbackend.entity.BannerVoucher;
import com.example.hotrohoctapbackend.enums.BannerPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerVoucherService {

    @Autowired
    private BannerVoucherRepository bannerVoucherRepository;

    public BannerVoucher createBannerVoucher(BannerVoucher bannerVoucher) {
        return bannerVoucherRepository.save(bannerVoucher);
    }

    public List<BannerVoucherDTO> getAllBanners() {
        List<BannerVoucher> banners = bannerVoucherRepository.findAll();
        return banners.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<BannerVoucherDTO> getAllBannerVouchers(Pageable pageable) {
        Page<BannerVoucher> bannerVouchers = bannerVoucherRepository.findAll(pageable);
        return bannerVouchers.map(this::convertToDTO);
    }

    // Hàm chuyển đổi từ BannerVoucher sang BannerVoucherDTO
    private BannerVoucherDTO convertToDTO(BannerVoucher bannerVoucher) {
        BannerVoucherDTO bannerVoucherDTO = new BannerVoucherDTO();
        bannerVoucherDTO.setId(bannerVoucher.getId());
        bannerVoucherDTO.setTitle(bannerVoucher.getTitle());
        bannerVoucherDTO.setImageUrl(bannerVoucher.getImageUrl());
        bannerVoucherDTO.setLink(bannerVoucher.getLink());

//        BannerPosition bannerPosition = bannerVoucher.getPosition();
        bannerVoucherDTO.setPosition(bannerVoucher.getPosition().name());
        bannerVoucherDTO.setPlatform(bannerVoucher.getPlatform().name()); // Chuyển Enum thành String
        bannerVoucherDTO.setType(bannerVoucher.getType().name()); // Chuyển Enum thành String
        bannerVoucherDTO.setStartDate(bannerVoucher.getStartDate());
        bannerVoucherDTO.setEndDate(bannerVoucher.getEndDate());
        bannerVoucherDTO.setStatus(bannerVoucher.getStatus());
        bannerVoucherDTO.setPriority(bannerVoucher.getPriority());
        bannerVoucherDTO.setDescription(bannerVoucher.getDescription());
        bannerVoucherDTO.setCreatedAt(bannerVoucher.getCreatedAt());
        bannerVoucherDTO.setUpdatedAt(bannerVoucher.getUpdatedAt());
        // Lấy Account ID thay vì thông tin Account đầy đủ
        if (bannerVoucher.getAccount() != null) {
            bannerVoucherDTO.setAccountId(bannerVoucher.getAccount().getId());
        }
        return bannerVoucherDTO;
    }


    public BannerVoucher updateBannerVoucher(BannerVoucher bannerVoucher) {
        return bannerVoucherRepository.save(bannerVoucher);
    }

    public boolean deleteBannerVoucher(Integer id) {
        if (bannerVoucherRepository.existsById(id)) {
            bannerVoucherRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
