package com.example.hotrohoctapbackend.service;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDicountDetailDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscounAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscountGetDTO;
import com.example.hotrohoctapbackend.dao.DiscountRepository;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Discount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {
    @Autowired
    private DiscountRepository discountRepository;

    public Page<AdminDiscountGetDTO> getDiscounts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<Object[]> results = discountRepository.findDiscounts(pageable);

        return results.map(row -> new AdminDiscountGetDTO(
                (Integer) row[0],                            // id
                (String) row[1],                             // description                 // discount_type
                row[2] != null ? ((BigDecimal) row[2]).doubleValue() : 0.0,  // Chuyển BigDecimal sang Double
                (String) row[3],                             // title
                (Boolean) row[4]                             // is_deleted
        ));
    }
    public AdminDicountDetailDTO getDiscountById(Integer id) {
        List<Object[]> discountDataList = discountRepository.findDiscountById(id);
        if (discountDataList != null && !discountDataList.isEmpty()) {
            Object[] data = discountDataList.get(0);
            if (data != null && data.length >= 6) {
                int discountId = (Integer) data[0];
                String description = (String) data[1];
                double discountValue = ((BigDecimal) data[2]).doubleValue();
                String title = (String) data[3];
                LocalDateTime endDate = ((java.sql.Timestamp) data[4]).toLocalDateTime();
                LocalDateTime startDate = ((java.sql.Timestamp) data[5]).toLocalDateTime();

                return new AdminDicountDetailDTO(discountId, description, discountValue, title, endDate, startDate);
            }
        }
        return null;
    }
    public Discount hideDiscountAdmin(int discountID) {
        // Tìm tài khoản theo ID
        Optional<Discount> accountOpt = discountRepository.findById(discountID);

        if (accountOpt.isPresent()) {
            Discount account = accountOpt.get();
            // Đặt isDeleted thành true và cập nhật deletedDate là ngày hiện tại
            account.setDeleted(true);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return discountRepository.save(account);
        } else {
            throw new RuntimeException("Test not found with id: " + discountID);
        }
    }
    public Discount showDiscountAdmin(int discountID) {
        // Tìm tài khoản theo ID
        Optional<Discount> accountOpt = discountRepository.findById(discountID);

        if (accountOpt.isPresent()) {
            Discount account = accountOpt.get();
            account.setDeleted(false);
            account.setDeletedDate(LocalDateTime.now());
            // Lưu thay đổi
            return discountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found with id: " + discountID);
        }
    }
    public Discount addDiscountAdmin(AdminDiscounAddDTO adminDiscounAddDTO) {
        // Validate discount details if necessary
        BigDecimal discountValue = adminDiscounAddDTO.getDiscount_value();

        if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than zero");
        }

        if (adminDiscounAddDTO.getStart_date().isAfter(adminDiscounAddDTO.getEnd_date())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Create a new Discount entity using DTO
        Discount discount = new Discount();
        discount.setTitle(adminDiscounAddDTO.getTitle());
        discount.setDescription(adminDiscounAddDTO.getDescription());
        discount.setDiscount_value(adminDiscounAddDTO.getDiscount_value());
        discount.setStart_date(adminDiscounAddDTO.getStart_date());
        discount.setEnd_date(adminDiscounAddDTO.getEnd_date());
        discount.setCreated_at(LocalDateTime.now());
        discount.setUpdated_at(LocalDateTime.now());
        discount.setDeleted(false);
        // Save the discount to the repository
        return discountRepository.save(discount);
    }
    public Discount updateDiscountAdmin(Integer discountId, AdminDiscounAddDTO adminDiscountAddDTO) {
        // Validate discount details if necessary
        if (adminDiscountAddDTO.getDiscount_value().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Discount value must be greater than zero");
        }

        if (adminDiscountAddDTO.getStart_date().isAfter(adminDiscountAddDTO.getEnd_date())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Find the existing discount by ID
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(() -> new IllegalArgumentException("Discount with ID " + discountId + " not found"));

        // Update the fields with the new data from the DTO
        discount.setTitle(adminDiscountAddDTO.getTitle());
        discount.setDescription(adminDiscountAddDTO.getDescription());
        discount.setDiscount_value(adminDiscountAddDTO.getDiscount_value());
        discount.setStart_date(adminDiscountAddDTO.getStart_date());
        discount.setEnd_date(adminDiscountAddDTO.getEnd_date());
        discount.setUpdated_at(LocalDateTime.now()); // Update the timestamp when modified

        // Save the updated discount to the repository
        return discountRepository.save(discount);
    }

}
