package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.ComboPackageDTOListSimple;
import com.example.hotrohoctapbackend.DTO.AdminV3.Bundle.CreateCourseBundleDTO;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.CourseBundleRepository;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.CourseBundle;
import com.example.hotrohoctapbackend.entity.CourseBundleItem;
import com.example.hotrohoctapbackend.mapper.ComboPackageMapper;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseBundleService {
    @Autowired
    private CourseBundleRepository courseBundleRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ImageKitService imageKitService;

    // Phương thức để lấy danh sách các gói combo với phân trang và filter theo tên gói và trạng thái
    public Page<CourseBundle> getCourseBundles(String title, Boolean status, Pageable pageable) {
        // Sử dụng phương thức gộp từ repository
        return courseBundleRepository.findByTitleAndStatus(title, status, pageable);
    }

    public List<ComboPackageDTOListSimple> getCourseBundlesList() {
        List<CourseBundle> courseBundles = courseBundleRepository.findByTitleAndStatusList();
        List<ComboPackageDTOListSimple> list = new ArrayList<>();
        for (CourseBundle item : courseBundles) {
            ComboPackageDTOListSimple comboPackageDTOListSimple = new ComboPackageDTOListSimple();
            comboPackageDTOListSimple = ComboPackageMapper.toDTOComboPackageDTOListSimple(item);
            list.add(comboPackageDTOListSimple);
        }
        return list;
    }

    public CourseBundle createCourseBundle(CreateCourseBundleDTO createCourseBundleDTO, MultipartFile imageFile) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        // Tìm các khóa học từ ID
        List<Course> courses = courseRepository.findAllById(createCourseBundleDTO.getCourseIds());

        // Tính giá combo
        BigDecimal totalOriginalPrice = BigDecimal.valueOf(createCourseBundleDTO.getOriginalPrice());
        BigDecimal totalPrice = BigDecimal.valueOf(createCourseBundleDTO.getPrice());

        // Tạo combo khóa học mới
        CourseBundle courseBundle = new CourseBundle();
        courseBundle.setTitle(createCourseBundleDTO.getName());
        courseBundle.setDescription(createCourseBundleDTO.getDescription());
        courseBundle.setPrice(totalPrice);
//        courseBundle.setOriginalPrice(totalOriginalPrice);
//        courseBundle.setDiscount(createCourseBundleDTO.getDiscount());
        if (createCourseBundleDTO.getStatus().equals("ACTIVE")) {
            courseBundle.setStatus(true);
        } else {
            courseBundle.setStatus(false);
        }

        // Xử lý file ảnh (nếu có)
        if (imageFile != null) {
            // Xử lý lưu ảnh (Ví dụ: lưu ảnh vào thư mục tạm thời hoặc S3, Cloud Storage)
            // Đối với ví dụ này, giả sử là lưu ảnh vào database dưới dạng URL hoặc lưu trữ file tạm
            String imageUrl = saveImageFile(imageFile);
            courseBundle.setImageUrl(imageUrl);
        }

        // Tạo danh sách CourseBundleItems để liên kết khóa học với combo
        List<CourseBundleItem> courseBundleItems = courses.stream()
                .map(course -> {
                    CourseBundleItem item = new CourseBundleItem();
                    item.setCourse(course);
                    item.setBundle(courseBundle);
                    return item;
                })
                .collect(Collectors.toList());
        courseBundle.setBundleItems(courseBundleItems);
        // Lưu gói combo vào cơ sở dữ liệu
        return courseBundleRepository.save(courseBundle);
    }

    // Phương thức để cập nhật gói combo khóa học
    public CourseBundle updateCourseBundle(Integer id, CreateCourseBundleDTO createCourseBundleDTO, MultipartFile imageFile) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        // Lấy gói combo hiện tại
        CourseBundle courseBundle = courseBundleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gói combo không tồn tại"));

        // Cập nhật thông tin gói combo
        courseBundle.setTitle(createCourseBundleDTO.getName());
        courseBundle.setDescription(createCourseBundleDTO.getDescription());
        courseBundle.setPrice(BigDecimal.valueOf(createCourseBundleDTO.getPrice()));
//        courseBundle.setOriginalPrice(BigDecimal.valueOf(createCourseBundleDTO.getOriginalPrice()));
//        courseBundle.setDiscount(createCourseBundleDTO.getDiscount());
        if (createCourseBundleDTO.getStatus().equals("ACTIVE")) {
            courseBundle.setStatus(true);
        } else {
            courseBundle.setStatus(false);
        }

        // Xử lý file ảnh (nếu có)
        if (imageFile != null) {
            String imageUrl = saveImageFile(imageFile);
            courseBundle.setImageUrl(imageUrl);
        }

        // Tạo danh sách CourseBundleItems để liên kết khóa học với combo
        List<Course> courses = courseRepository.findAllById(createCourseBundleDTO.getCourseIds());
        List<CourseBundleItem> courseBundleItems = courses.stream()
                .map(course -> {
                    CourseBundleItem item = new CourseBundleItem();
                    item.setCourse(course);
                    item.setBundle(courseBundle);
                    return item;
                })
                .collect(Collectors.toList());

        courseBundle.updateBundleItems(courseBundleItems);


        // Lưu gói combo vào cơ sở dữ liệu
        return courseBundleRepository.save(courseBundle);
    }

    // Phương thức giả lập lưu ảnh vào thư mục (hoặc S3)
    private String saveImageFile(MultipartFile imageFile) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {
        Result imageUploadResult = imageKitService.uploadFromBytes(imageFile);
        return imageUploadResult.getUrl();
    }

    public CourseBundle toggleStatus(Integer id, String status) {
        // Lấy gói combo từ cơ sở dữ liệu
        CourseBundle courseBundle = courseBundleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gói combo không tồn tại"));

        // Cập nhật trạng thái
        courseBundle.setStatus("ACTIVE".equals(status));


        // Lưu gói combo với trạng thái mới
        return courseBundleRepository.save(courseBundle);
    }

    public CourseBundle softDeleteCourseBundle(Integer id) {
        // Lấy gói combo từ cơ sở dữ liệu
        CourseBundle courseBundle = courseBundleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gói combo không tồn tại"));

        // Đánh dấu gói combo là đã xóa mềm
        courseBundle.setDeleted(true);
        courseBundle.setStatus(false);

        // Lưu gói combo với trạng thái xóa mềm
        return courseBundleRepository.save(courseBundle);
    }
}
