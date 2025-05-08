package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminDicountDetailDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscounAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscountGetDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.ApplyDiscountRequest;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.DiscountItemDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Discount.DiscountItemDTOResponsive;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Discount;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/discounts")
public class DiscountController {
    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/getall")
    public Page<AdminDiscountGetDTO> getDiscounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return discountService.getDiscounts(page, size);
    }

    @GetMapping("detail/{id}")
    public AdminDicountDetailDTO getDiscountById(@PathVariable int id) {
        return discountService.getDiscountById(id);
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideDiscountAdmin(@PathVariable int id) {
        try {
            Discount hidedDiscount = discountService.hideDiscountAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showDiscountAdmin(@PathVariable int id) {
        try {
            Discount showDiscount = discountService.showDiscountAdmin(id);
            return ResponseEntity.ok().body("Account with ID " + id + " marked as deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found with ID: " + id);
        }
    }

    // API Thêm Discount
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addDiscount(@RequestBody DiscountItemDTO discountDTO) {
        try {
            ApiResponse<DiscountItemDTOResponsive> response = discountService.addDiscount(discountDTO);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            ApiResponse<String> errorResponse = new ApiResponse<>(500, "Đã có lỗi xảy ra, vui lòng thử lại.", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    // API Sửa Discount
    @PutMapping("/update/{discountId}")
    public ResponseEntity<DiscountItemDTOResponsive> updateDiscount(
            @PathVariable int discountId, @RequestBody DiscountItemDTO discountDTO) {
        DiscountItemDTOResponsive updatedDiscount = discountService.updateDiscount(discountId, discountDTO);
        return ResponseEntity.ok(updatedDiscount);
    }

    @GetMapping("/filter-all")
    public Page<DiscountItemDTOResponsive> getDiscountsPageAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String discountType) {

        return discountService.getDiscountsWithPagination(title, discountType, page, size);
    }

    @GetMapping("/new/code")
    public ResponseEntity<ApiResponse<String>> getLatestDiscountCodeAndIncrease() {
        ApiResponse<String> response = discountService.getLatestDiscountCode();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/toggle-status/{discountId}")
    public ResponseEntity<ApiResponse<String>> toggleDiscountStatus(@PathVariable int discountId) {
        ApiResponse<String> response = discountService.toggleDiscountStatus(discountId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/apply")
    public ResponseEntity<ApiResponse<String>> applyDiscount(@RequestBody ApplyDiscountRequest request) {
        try {
            // Gọi service để áp dụng mã giảm giá (cả thêm mới và cập nhật)
            ApiResponse<String> response = discountService.applyDiscount(request);

            // Trả về phản hồi thành công
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            // Trả về lỗi nếu có vấn đề
            return ResponseEntity.status(400).body(new ApiResponse<>(400, e.getMessage(), null));
        }
    }


}
