package com.example.hotrohoctapbackend.mapper;

import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.ComboPackageDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.ComboPackageDTOListSimple;
import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.CoursePackageDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Subscription.MembershipPackageDTO;
import com.example.hotrohoctapbackend.entity.CourseBundle;
import com.example.hotrohoctapbackend.entity.CourseBundleItem;
import com.example.hotrohoctapbackend.entity.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComboPackageMapper {
    public static ComboPackageDTO toDTO(CourseBundle courseBundle) {
        ComboPackageDTO dto = new ComboPackageDTO();
        dto.setId(courseBundle.getId());
        dto.setName(courseBundle.getTitle());
        dto.setDescription(courseBundle.getDescription());
        dto.setPrice(courseBundle.getPrice().doubleValue());
        dto.setOriginalPrice(courseBundle.getPrice().doubleValue());
        dto.setImageUrl(courseBundle.getImageUrl());

        //NOTE CHƯA XỬ LÝ
        dto.setDiscount(0);

        List<CoursePackageDTO> coursePackageDTOList = new ArrayList<>();
        for (CourseBundleItem item : courseBundle.getBundleItems()) {
            CoursePackageDTO coursePackageDTO = new CoursePackageDTO();
            coursePackageDTO.setId(item.getId());
            coursePackageDTO.setTitle(item.getCourse().getTitle());
            coursePackageDTO.setPrice(item.getCourse().getPrice());
            coursePackageDTO.setAuthor(item.getCourse().getAuthor());
            coursePackageDTO.setImageUrl(item.getCourse().getImage_url());
            coursePackageDTOList.add(coursePackageDTO);
        }

        dto.setCourses(coursePackageDTOList);
        dto.setStatus(courseBundle.isStatus() ? "ACTIVE" : "INACTIVE");

        int enrolledCount = (courseBundle.getEnrolled_courses() != null) ? courseBundle.getEnrolled_courses().size() : 0;

        dto.setSalesCount(enrolledCount);

        dto.setCreatedAt(courseBundle.getCreatedAt());
        dto.setUpdatedAt(courseBundle.getUpdatedAt());
        return dto;
    }

    public static ComboPackageDTOListSimple toDTOComboPackageDTOListSimple(CourseBundle courseBundle) {
        ComboPackageDTOListSimple dto = new ComboPackageDTOListSimple();
        dto.setId(courseBundle.getId());
        dto.setName(courseBundle.getTitle());
        dto.setDescription(courseBundle.getDescription());
        dto.setPrice(courseBundle.getPrice().doubleValue());
        dto.setImageUrl(courseBundle.getImageUrl());
        dto.setStatus(courseBundle.isStatus() ? "ACTIVE" : "INACTIVE");
        dto.setCreatedAt(courseBundle.getCreatedAt());
        dto.setUpdatedAt(courseBundle.getUpdatedAt());
        return dto;
    }
}
