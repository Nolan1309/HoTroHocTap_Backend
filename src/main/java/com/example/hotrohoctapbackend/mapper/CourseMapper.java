package com.example.hotrohoctapbackend.mapper;

import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.ComboPackageDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.CoursePackageDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Course.CourseForListAdminDTO;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.CourseBundle;
import com.example.hotrohoctapbackend.entity.CourseBundleItem;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {
    public static CourseForListAdminDTO toDTOForListAdmin(Course course) {
        CourseForListAdminDTO dto = new CourseForListAdminDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setImageUrl(course.getImage_url());
        Integer accountId = course.getAccount().getId();
        dto.setAccountId(accountId.toString());

        dto.setCost(course.getCost());
        dto.setPrice(course.getPrice());


        Integer courseCategoryId = course.getCategory().getId();
        dto.setCourseCategoryId(courseCategoryId.toString());
        return dto;
    }
}
