package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.DTO.Admin.AdminPaymentDetailDTO;
import com.example.hotrohoctapbackend.DTO.PaymentDetailDTO;
import com.example.hotrohoctapbackend.dao.CourseRepository;
import com.example.hotrohoctapbackend.dao.PaymentDetailRepository;
import com.example.hotrohoctapbackend.dao.PaymentRepository;
import com.example.hotrohoctapbackend.entity.Course;
import com.example.hotrohoctapbackend.entity.Payment;
import com.example.hotrohoctapbackend.entity.PaymentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentDetailService {
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Hàm thêm mới PaymentDetail
    public PaymentDetail createPaymentDetail(PaymentDetailDTO paymentDetailDTO) {

        // Tạo đối tượng PaymentDetail mới
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setPrice(paymentDetailDTO.getPrice());
        paymentDetail.setCourseTitle(paymentDetailDTO.getCourseTitle());

        Payment payment = new Payment();
        payment.setId(paymentDetailDTO.getPaymentId());
        paymentDetail.setPayment(payment);

        Course course = new Course();
        course.setId(paymentDetailDTO.getCourseId());
        paymentDetail.setCourse(course);


        return paymentDetailRepository.save(paymentDetail);
    }

    public List<PaymentDetail> findByPaymentID(Integer id){
        return paymentDetailRepository.findPaymentByPaymentId(id);
    }
    public PaymentDetailService(PaymentDetailRepository paymentDetailRepository) {
        this.paymentDetailRepository = paymentDetailRepository;
    }

    public List<AdminPaymentDetailDTO> getCoursePaymentDetailsByPaymentId(Integer paymentId) {
        List<Object[]> rawResults = paymentDetailRepository.findCoursePaymentDetailsByPaymentId(paymentId);

        return rawResults.stream().map(result ->
                new AdminPaymentDetailDTO(
                        (String) result[0],      // course_name
                        ((Number) result[1]).doubleValue()  // price
                )
        ).collect(Collectors.toList());
    }
}
