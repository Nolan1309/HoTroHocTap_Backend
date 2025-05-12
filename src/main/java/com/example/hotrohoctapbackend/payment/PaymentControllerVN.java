package com.example.hotrohoctapbackend.payment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "${allowed.origins}", allowCredentials = "true")
@RequestMapping("/api/payment/vnpay")
@RequiredArgsConstructor
public class PaymentControllerVN {

    @Autowired
    private final PaymentServiceVNPay paymentService;

    @GetMapping("/vn-pay")
    public ResponseEntity<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.createVnPayPayment(request));
    }


    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String status = request.getParameter("vnp_ResponseCode");

        if ("00".equals(status)) {
            // Redirect to success page (frontend page)
            response.sendRedirect("http://localhost:3000/thanh-toan/logic");
        } else {
            // Redirect to failure page (frontend page)
            response.sendRedirect("http://localhost:3000/khoa-hoc/thanh-toan/fail");
        }
    }
}
