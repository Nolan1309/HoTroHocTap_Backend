package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Subscription.CreateMembershipPackageDTO;
import com.example.hotrohoctapbackend.dao.SubcriptionRepository;
import com.example.hotrohoctapbackend.entity.Subscription;
import com.example.hotrohoctapbackend.enums.SubscriptionFeature;
import com.example.hotrohoctapbackend.enums.SubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    @Autowired
    private SubcriptionRepository subscriptionRepository;

    public Page<Subscription> getSubscriptions(String name, SubscriptionStatus status, Integer durationDays, Pageable pageable) {
        return subscriptionRepository.findByFilters(name, status, durationDays, pageable);
    }

    public Subscription createMembershipPackage(CreateMembershipPackageDTO createMembershipPackageDTO) {
        Subscription subscription = new Subscription();

        // Gán giá trị từ DTO vào Entity
        subscription.setName(createMembershipPackageDTO.getName());
        subscription.setDescription(createMembershipPackageDTO.getDescription());
        subscription.setPrice(new BigDecimal(createMembershipPackageDTO.getPrice()));
        subscription.setDuration_days(createMembershipPackageDTO.getDuration());
//        subscription.setDiscountPercentage(createMembershipPackageDTO.getDiscountPercentage());
        SubscriptionStatus status = SubscriptionStatus.valueOf(createMembershipPackageDTO.getStatus());
        subscription.setStatus(status);

        if (createMembershipPackageDTO.getFeatures() != null) {
            subscription.setFeatures(createMembershipPackageDTO.getFeatures().stream()
                    .map(featureName -> SubscriptionFeature.valueOf(featureName))
                    .collect(Collectors.toList()));
        }

        // Lưu gói thành viên vào cơ sở dữ liệu
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateMembershipPackage(Integer id, CreateMembershipPackageDTO updateDTO) {
        // Tìm gói thành viên hiện tại theo ID
        Subscription subscription = subscriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Gói thành viên không tồn tại"));

        // Cập nhật thông tin từ DTO
        subscription.setName(updateDTO.getName());
        subscription.setDescription(updateDTO.getDescription());
        subscription.setPrice(new BigDecimal(updateDTO.getPrice()));
        subscription.setDuration_days(updateDTO.getDuration());
//        subscription.setDiscountPercentage(updateDTO.getDiscountPercentage());
        SubscriptionStatus status = SubscriptionStatus.valueOf(updateDTO.getStatus());
        subscription.setStatus(status);

        // Chuyển các tính năng từ List<String> thành List<SubscriptionFeature>
        List<SubscriptionFeature> features = updateDTO.getFeatures().stream()
                .map(SubscriptionFeature::valueOf)  // Chuyển từ enum key
                .collect(Collectors.toList());

        subscription.setFeatures(features);

        // Lưu gói thành viên cập nhật vào cơ sở dữ liệu
        return subscriptionRepository.saveAndFlush(subscription);
    }

    public Subscription toggleStatus(Integer id) {
        // Tìm gói thành viên theo ID
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gói thành viên không tồn tại"));

        // Đổi trạng thái
        String newStatus = "INACTIVE";
        if (subscription.getStatus() == SubscriptionStatus.INACTIVE) {
            newStatus = "ACTIVE";
        }


        SubscriptionStatus status = SubscriptionStatus.valueOf(newStatus);
        subscription.setStatus(status);

        // Lưu gói thành viên với trạng thái mới
        return subscriptionRepository.saveAndFlush(subscription);
    }

    public void softDelete(Integer id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gói thành viên không tồn tại"));
        subscription.setStatus(SubscriptionStatus.INACTIVE);
        subscription.setDeleted(true);
        subscription.setDeletedDate(LocalDateTime.now());
        subscriptionRepository.save(subscription);
    }
}
