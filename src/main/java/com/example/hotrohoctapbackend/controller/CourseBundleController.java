package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.*;
import com.example.hotrohoctapbackend.dao.Course_DiscountRepository;
import com.example.hotrohoctapbackend.entity.CourseBundle;
import com.example.hotrohoctapbackend.entity.CourseBundleItem;
import com.example.hotrohoctapbackend.entity.Course_Discount;
import com.example.hotrohoctapbackend.enums.DiscountStatus;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.mapper.ComboPackageMapper;
import com.example.hotrohoctapbackend.service.CourseBundleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.imagekit.sdk.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/course-bundle")
public class CourseBundleController {
    @Autowired
    private CourseBundleService courseBundleService;

    @Autowired
    private Course_DiscountRepository courseDiscountRepository;

    // API để lấy danh sách các gói combo khóa học với phân trang và filter theo tên gói và trạng thái
    @GetMapping
    public ApiResponse<Page<ComboPackageDTO>> getCourseBundles(
            @RequestParam(required = false) String title,  // Tên gói (filter, có thể null)
            @RequestParam(required = false) Boolean status,  // Trạng thái gói (filter, có thể null)
            Pageable pageable) {  // Phân trang

        // Lấy dữ liệu từ service
        Page<CourseBundle> courseBundles = courseBundleService.getCourseBundles(title, status, pageable);

        // Chuyển dữ liệu từ Entity sang DTO (Bạn có thể viết phương thức Mapper để chuyển từ Entity sang DTO)
        Page<ComboPackageDTO> courseBundleDTOs = courseBundles.map(courseBundle -> {
            ComboPackageDTO dto = new ComboPackageDTO();
            dto.setId(courseBundle.getId());
            dto.setName(courseBundle.getTitle());
            dto.setDescription(courseBundle.getDescription());
            dto.setPrice(courseBundle.getPrice().doubleValue());
            dto.setOriginalPrice(courseBundle.getPrice().doubleValue());
            dto.setImageUrl(courseBundle.getImageUrl());

            //NOTE CHƯA XỬ LÝ
            Optional<Course_Discount> courseDiscountOpt = courseDiscountRepository.findByCourseBundleId(courseBundle.getId());

            if (courseDiscountOpt.isPresent() && courseDiscountOpt.get().getDiscount() != null && courseDiscountOpt.get().getDiscount().getStatus() == DiscountStatus.ACTIVE) {
                int intValue = courseDiscountOpt.get().getDiscount().getDiscountValue().intValue();
                dto.setDiscount(intValue);
            } else {
                dto.setDiscount(0);
            }

            List<CoursePackageDTO> coursePackageDTOList = new ArrayList<>();
            for (CourseBundleItem item : courseBundle.getBundleItems()) {
                CoursePackageDTO coursePackageDTO = new CoursePackageDTO();
                coursePackageDTO.setId(item.getCourse().getId());
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
        });

        return new ApiResponse<>(200, "Danh sách các gói combo khóa học", courseBundleDTOs);
    }

    @GetMapping("/list-all")
    public ApiResponse<List<ComboPackageDTOListSimple>> getAllCourseBundlesList() {
        // Lấy dữ liệu từ service
        List<ComboPackageDTOListSimple> courseBundles = courseBundleService.getCourseBundlesList();
        return new ApiResponse<>(200, "Danh sách các gói combo khóa học dạng LIST", courseBundles);
    }

    @PostMapping("/create")
    public ApiResponse<ComboPackageDTO> createCourseBundle(
            @RequestPart("data") String data,  // Nhận dữ liệu JSON từ frontend dưới dạng chuỗi
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        // Parse dữ liệu JSON từ "data" thành đối tượng CreateCourseBundleDTO
        ObjectMapper objectMapper = new ObjectMapper();
        CreateCourseBundleDTO createCourseBundleDTO = objectMapper.readValue(data, CreateCourseBundleDTO.class);

        // Gọi service để tạo gói combo khóa học mới
        CourseBundle newCourseBundle = courseBundleService.createCourseBundle(createCourseBundleDTO, imageFile);
        ComboPackageDTO newCourseBundleDTO = ComboPackageMapper.toDTO(newCourseBundle);

        // Trả về DTO với phản hồi
        return new ApiResponse<>(200, "Tạo gói combo khóa học thành công!", newCourseBundleDTO);
    }

    // API để cập nhật gói combo khóa học
    @PutMapping("/{id}")
    public ApiResponse<ComboPackageDTO> updateCourseBundle(@PathVariable Integer id,
                                                           @RequestPart("data") String data,
                                                           @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                           HttpServletRequest request) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        // Parse JSON data to object
        CreateCourseBundleDTO createCourseBundleDTO = new ObjectMapper().readValue(data, CreateCourseBundleDTO.class);

        // Call service to update existing course bundle
        CourseBundle updatedCourseBundle = courseBundleService.updateCourseBundle(id, createCourseBundleDTO, imageFile);

        ComboPackageDTO updateCourseBundleDTO = ComboPackageMapper.toDTO(updatedCourseBundle);
        return new ApiResponse<>(200, "Cập nhật gói combo khóa học thành công!", updateCourseBundleDTO);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<ComboPackageDTO> toggleStatus(@PathVariable Integer id, @RequestBody StatusUpdateDTO statusUpdateDTO) {
        try {
            // Gọi service để cập nhật trạng thái của gói combo
            CourseBundle updatedCourseBundle = courseBundleService.toggleStatus(id, statusUpdateDTO.getStatus());
            ComboPackageDTO comboPackageDTO = ComboPackageMapper.toDTO(updatedCourseBundle);
            // Trả về kết quả cập nhật
            return new ApiResponse<>(200, "Trạng thái gói combo đã được thay đổi!", comboPackageDTO);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi thay đổi trạng thái gói combo", null);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<ComboPackageDTO> softDeleteCourseBundle(@PathVariable Integer id) {
        try {
            // Gọi service để thực hiện xóa mềm
            CourseBundle courseBundle = courseBundleService.softDeleteCourseBundle(id);
            ComboPackageDTO comboPackageDTO = ComboPackageMapper.toDTO(courseBundle);
            // Trả về phản hồi thành công
            return new ApiResponse<>(200, "Gói combo đã được xóa mềm thành công!", comboPackageDTO);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi xóa gói combo", null);
        }
    }
}
