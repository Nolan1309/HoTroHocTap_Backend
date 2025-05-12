package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.AdminV3.Subscription.CreateMembershipPackageDTO;
import com.example.hotrohoctapbackend.DTO.AdminV3.Subscription.MembershipPackageDTO;
import com.example.hotrohoctapbackend.entity.Subscription;
import com.example.hotrohoctapbackend.enums.SubscriptionStatus;
import com.example.hotrohoctapbackend.exception.ApiResponse;
import com.example.hotrohoctapbackend.mapper.MembershipPackageMapper;
import com.example.hotrohoctapbackend.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping()
    public ApiResponse<Page<MembershipPackageDTO>> getSubscriptions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer duration,
            Pageable pageable) {
        SubscriptionStatus status1 = null;
        if (status != null) {
            status1 = SubscriptionStatus.valueOf(status);
        }

        Page<Subscription> subscriptionPage = subscriptionService.getSubscriptions(name, status1, duration, pageable);


        Page<MembershipPackageDTO> dtoPage = subscriptionPage.map(MembershipPackageMapper::toDTO);

        return new ApiResponse<>(200, "Danh sách các gói thành viên", dtoPage);
    }

    @PostMapping("/create")
    public ApiResponse<?> createMembershipPackage(@RequestBody CreateMembershipPackageDTO createMembershipPackageDTO) {
        try {

            Subscription newSubscription = subscriptionService.createMembershipPackage(createMembershipPackageDTO);

            return new ApiResponse<>(200, "Tạo gói thành viên thành công", newSubscription);
        } catch (Exception e) {

            return new ApiResponse<>(500, "Đã xảy ra lỗi khi tạo gói thành viên: " + e.getMessage(), null);
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Subscription> updateMembershipPackage(
            @PathVariable("id") Integer id,
            @RequestBody CreateMembershipPackageDTO updateDTO) {

        try {

            Subscription updatedSubscription = subscriptionService.updateMembershipPackage(id, updateDTO);


            return new ApiResponse<>(200, "Cập nhật gói thành viên thành công!", updatedSubscription);
        } catch (Exception e) {

            return new ApiResponse<>(500, "Có lỗi xảy ra khi cập nhật gói thành viên", null);
        }
    }

    @PutMapping("/{id}/status")
    public ApiResponse<?> toggleStatus(@PathVariable("id") Integer id) {
        try {
            // Gọi service để thay đổi trạng thái gói thành viên
            Subscription updatedSubscription = subscriptionService.toggleStatus(id);
            return new ApiResponse<>(200, "Trạng thái gói thành viên đã được thay đổi", updatedSubscription);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi thay đổi trạng thái gói thành viên", null);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> softDelete(@PathVariable("id") Integer id) {
        try {
            // Gọi service để thực hiện xóa mềm
            subscriptionService.softDelete(id);
            return new ApiResponse<>(200, "Gói thành viên đã được xóa mềm", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Có lỗi xảy ra khi xóa mềm gói thành viên", null);
        }
    }
}
