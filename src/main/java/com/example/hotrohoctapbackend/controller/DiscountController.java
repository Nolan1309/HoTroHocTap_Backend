package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminDicountDetailDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscounAddDTO;
import com.example.hotrohoctapbackend.DTO.Admin.AdminDiscountGetDTO;
import com.example.hotrohoctapbackend.entity.Comment;
import com.example.hotrohoctapbackend.entity.Discount;
import com.example.hotrohoctapbackend.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
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
    @PostMapping("/add")
    public ResponseEntity<Discount> addDiscount(@RequestBody AdminDiscounAddDTO adminDiscountAddDTO) {
        try {
            Discount discount = discountService.addDiscountAdmin(adminDiscountAddDTO);
            return ResponseEntity.ok(discount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Handle validation errors
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Integer id, @RequestBody AdminDiscounAddDTO adminDiscountAddDTO) {
        try {
            Discount updatedDiscount = discountService.updateDiscountAdmin(id, adminDiscountAddDTO);
            return ResponseEntity.ok(updatedDiscount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Return an error response if validation fails
        }
    }

}
