package com.example.hotrohoctapbackend.service;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDicountDetailDTO;
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
}
