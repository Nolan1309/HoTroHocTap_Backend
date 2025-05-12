package com.example.hotrohoctapbackend.mapper;

import com.example.hotrohoctapbackend.DTO.AdminV3.Subscription.MembershipPackageDTO;
import com.example.hotrohoctapbackend.entity.Subscription;

import java.util.stream.Collectors;

public class MembershipPackageMapper {
    public static MembershipPackageDTO toDTO(Subscription subscription) {
        MembershipPackageDTO dto = new MembershipPackageDTO();
        dto.setId(subscription.getId());
        dto.setName(subscription.getName());
        dto.setDescription(subscription.getDescription());

        dto.setPrice(subscription.getPrice().doubleValue()); // assuming price is stored as BigDecimal
        dto.setDuration(subscription.getDuration_days());
        dto.setFeatures(subscription.getFeatures().stream()
                .map(feature -> feature.getDescription()) // assuming SubscriptionFeature is an enum
                .collect(Collectors.toList()));

//        dto.setDiscountPercentage(10);


        dto.setStatus(subscription.getStatus().toString()); // assuming SubscriptionStatus is an enum
        dto.setSubscribersCount(subscription.getSubscriptionsAccountList().size());
        dto.setCreatedAt(subscription.getCreated_at());
        dto.setUpdatedAt(subscription.getUpdated_at());
        dto.setSubscribersCount(subscription.getSubscriptionsAccountList() != null ? subscription.getSubscriptionsAccountList().size() : 0);

        return dto;
    }
}
