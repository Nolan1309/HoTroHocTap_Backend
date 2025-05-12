package com.example.hotrohoctapbackend.controller;

import com.example.hotrohoctapbackend.DTO.Admin.AdminPaymentDetailDTO;
import com.example.hotrohoctapbackend.DTO.PaymentDetailDTO;
import com.example.hotrohoctapbackend.DTO.PaymentResponseDTO;
import com.example.hotrohoctapbackend.DTO.User.CourseDetailDTO_User;
import com.example.hotrohoctapbackend.entity.PaymentDetail;
import com.example.hotrohoctapbackend.service.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RestController
@RequestMapping("/api/payment-details")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailService paymentDetailService;


    // API để thêm mới PaymentDetail
    @PostMapping("/add")
    public ResponseEntity<PaymentDetailDTO> createPaymentDetail(@RequestBody PaymentDetailDTO paymentDetailDTO) {
        PaymentDetail paymentDetail = paymentDetailService.createPaymentDetail(paymentDetailDTO);

        PaymentDetailDTO dto = new PaymentDetailDTO();
        dto.setId(paymentDetail.getId());
        dto.setPrice(paymentDetail.getPrice());
        dto.setCourseTitle(paymentDetail.getCourseTitle());
        dto.setPaymentId(paymentDetailDTO.getPaymentId());
        dto.setCourseId(paymentDetailDTO.getCourseId());

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<List<PaymentDetailDTO>> getPaymentById(@PathVariable("id") Integer id) {
        List<PaymentDetail> payment = paymentDetailService.findByPaymentID(id);

        List<PaymentDetailDTO> listdto = new ArrayList<>();
        for (PaymentDetail item : payment) {
            PaymentDetailDTO dto = new PaymentDetailDTO();
            dto.setId(item.getId());
            dto.setPaymentId(item.getPayment().getId());
            dto.setPrice(item.getPrice());
            dto.setCourseTitle(item.getCourseTitle());
            dto.setCourseId(item.getCourse().getId());
            listdto.add(dto);
        }

        if (listdto.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            // Trả về Payment và mã 200 OK
        } else {
            return new ResponseEntity<>(listdto, HttpStatus.OK);
        }
    }

    @GetMapping("/admin/{paymentId}")
    public List<AdminPaymentDetailDTO> getCoursePaymentDetailsByPaymentId(@PathVariable Integer paymentId) {
        return paymentDetailService.getCoursePaymentDetailsByPaymentId(paymentId);
    }


}
