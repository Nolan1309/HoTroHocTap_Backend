package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Banner.BannerVoucherDTO;
import com.example.hotrohoctapbackend.config.ImageKitService;
import com.example.hotrohoctapbackend.dao.AccountRepository;
import com.example.hotrohoctapbackend.dao.BannerVoucherRepository;
import com.example.hotrohoctapbackend.entity.Account;
import com.example.hotrohoctapbackend.entity.BannerVoucher;
import com.example.hotrohoctapbackend.enums.BannerPosition;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.BannerVoucherService;

import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/banner-voucher")
public class BannerVoucherController {
    @Autowired
    private BannerVoucherService bannerVoucherService;

    @Autowired
    private BannerVoucherRepository bannerVoucherRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ImageKitService imageKitService;


    @PostMapping
    public ResponseEntity<ApiResponse<String>> createBanner(
            @RequestParam("title") String title,
            @RequestParam("image") MultipartFile image,
            @RequestParam("link") String link,
            @RequestParam("position") String position,
            @RequestParam("platform") String platform,
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("status") boolean status,
            @RequestParam("priority") int priority,
            @RequestParam("description") String description,
            @RequestParam("accountId") Integer accountId) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        // Lấy Account từ ID
        Account account = accountRepository.findById(accountId).get();

        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Account not found", null));
        }
        Result imageCheck = imageKitService.uploadFromBytes(image);

        // Tạo Banner
        BannerVoucher bannerVoucher = new BannerVoucher();
        bannerVoucher.setTitle(title);
        bannerVoucher.setImageUrl(imageCheck.getUrl());
        bannerVoucher.setLink(link);
        BannerPosition bannerPosition = BannerPosition.valueOf(position);
        bannerVoucher.setPosition(bannerPosition);
        bannerVoucher.setPlatform(BannerVoucher.Platform.valueOf(platform)); // Convert từ String sang Enum
        bannerVoucher.setType(BannerVoucher.BannerType.valueOf(type)); // Convert từ String sang Enum
        ZonedDateTime startZonedDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime endZonedDateTime = ZonedDateTime.parse(endDate);

        // Convert sang UTC (chuyển đổi về ZoneOffset.UTC)
        LocalDateTime startLocalDateTime = startZonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endLocalDateTime = endZonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();


        bannerVoucher.setStartDate(startLocalDateTime); // Convert String sang LocalDateTime
        bannerVoucher.setEndDate(endLocalDateTime); // Convert String sang LocalDateTime
        bannerVoucher.setStatus(status);
        bannerVoucher.setPriority(priority);
        bannerVoucher.setDescription(description);
        bannerVoucher.setAccount(account); // Gán Account vào Banner
        bannerVoucher.setCreatedAt(LocalDateTime.now());
        bannerVoucher.setUpdatedAt(LocalDateTime.now());

        // Lưu Banner
        BannerVoucher createdBanner = bannerVoucherService.createBannerVoucher(bannerVoucher);

        ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(), "Banner created successfully", imageCheck.getUrl());
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<BannerVoucherDTO>> getAllBannerVouchers(Pageable pageable) {
        Page<BannerVoucherDTO> bannerVouchers = bannerVoucherService.getAllBannerVouchers(pageable);
        return ResponseEntity.ok(bannerVouchers);
    }

    @GetMapping("/list")
    public ApiResponse<List<BannerVoucherDTO>> getAllBanners() {
        try {
            List<BannerVoucherDTO> bannerVoucherDTOs = bannerVoucherService.getAllBanners();
            return new ApiResponse<>(200, "Success", bannerVoucherDTOs);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Internal Server Error", null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateBannerVoucher(
            @PathVariable Integer id,
            @RequestParam("title") String title,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("link") String link,
            @RequestParam("position") String position,
            @RequestParam("platform") String platform,
            @RequestParam("type") String type,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("status") boolean status,
            @RequestParam("priority") int priority,
            @RequestParam("description") String description,
            @RequestParam("accountId") Integer accountId) throws IOException, ForbiddenException, TooManyRequestsException, InternalServerException, UnauthorizedException, BadRequestException, UnknownException {

        // Lấy Account từ ID
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Lấy BannerVoucher từ ID
        BannerVoucher bannerVoucher = bannerVoucherRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Banner not found"));

        String imageUrl = bannerVoucher.getImageUrl(); // Lấy URL hình ảnh hiện tại (nếu có)
        if (image != null && !image.isEmpty()) {
            Result imageCheck = imageKitService.uploadFromBytes(image); // Nếu có ảnh mới, tải lên
            imageUrl = imageCheck.getUrl(); // Cập nhật URL hình ảnh
        }
        // Cập nhật BannerVoucher
        bannerVoucher.setTitle(title);
        bannerVoucher.setImageUrl(imageUrl); // Cập nhật URL hình ảnh
        bannerVoucher.setLink(link);
        BannerPosition bannerPosition = BannerPosition.valueOf(position);
        bannerVoucher.setPosition(bannerPosition);
        bannerVoucher.setPlatform(BannerVoucher.Platform.valueOf(platform)); // Convert từ String sang Enum
        bannerVoucher.setType(BannerVoucher.BannerType.valueOf(type)); // Convert từ String sang Enum

        // Parse ngày bắt đầu và kết thúc
        ZonedDateTime startZonedDateTime = ZonedDateTime.parse(startDate);
        ZonedDateTime endZonedDateTime = ZonedDateTime.parse(endDate);

        // Convert sang UTC (chuyển đổi về ZoneOffset.UTC)
        LocalDateTime startLocalDateTime = startZonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endLocalDateTime = endZonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        bannerVoucher.setStartDate(startLocalDateTime); // Set thời gian bắt đầu
        bannerVoucher.setEndDate(endLocalDateTime); // Set thời gian kết thúc
        bannerVoucher.setStatus(status);
        bannerVoucher.setPriority(priority);
        bannerVoucher.setDescription(description);
        bannerVoucher.setAccount(account); // Gán Account vào Banner
        bannerVoucher.setUpdatedAt(LocalDateTime.now()); // Cập nhật thời gian sửa đổi

        BannerVoucher updatedBannerVoucher = bannerVoucherService.updateBannerVoucher(bannerVoucher);
        ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Banner updated successfully", imageUrl);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBannerVoucher(@PathVariable Integer id) {
        try {
            boolean isDeleted = bannerVoucherService.deleteBannerVoucher(id);
            if (isDeleted) {
                return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Banner deleted successfully", "Banner has been marked as deleted."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Banner not found", null));
            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid ID provided", null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while deleting the banner", null));
        }
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<ApiResponse<String>> toggleBannerStatus(@PathVariable Integer id) {
        try {
            // Tìm BannerVoucher theo ID
            BannerVoucher bannerVoucher = bannerVoucherRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Banner not found"));

            // Đổi trạng thái của BannerVoucher
            bannerVoucher.setStatus(!bannerVoucher.getStatus());  // Đảo ngược trạng thái

            // Cập nhật thời gian sửa đổi
            bannerVoucher.setUpdatedAt(LocalDateTime.now());

            // Lưu lại BannerVoucher đã được cập nhật
            bannerVoucherRepository.save(bannerVoucher);

            // Trả về phản hồi
            String statusMessage = bannerVoucher.getStatus() ? "Banner activated" : "Banner deactivated";
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), statusMessage, null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception ex) {
            // Bắt lỗi nếu không tìm thấy banner
            ApiResponse<String> apiResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Banner not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }
    }

}
