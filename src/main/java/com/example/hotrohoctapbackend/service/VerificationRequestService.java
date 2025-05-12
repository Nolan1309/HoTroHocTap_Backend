package com.example.hotrohoctapbackend.service;

import com.example.hotrohoctapbackend.dao.VerificationRequestRepository;
import com.example.hotrohoctapbackend.entity.VerificationRequest;
import com.example.hotrohoctapbackend.service.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class VerificationRequestService {

    @Autowired
    private VerificationRequestRepository verificationRequestRepository;


    @Autowired
    private EmailService emailService;

    public String generateOTP() {
        return String.valueOf((int) (Math.random() * 9000 + 1000)); // Tạo OTP 4 số
    }

    public void createVerificationRequest(String fullname, String email, String password, String birthday, String phone) {
        String otp = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5); // OTP hết hạn sau 5 phút

        VerificationRequest request = new VerificationRequest();
        request.setFullname(fullname);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);
        // Chỉ định định dạng cho ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Chuyển đổi chuỗi birthday sang LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(birthday, formatter);
        request.setBirthday(dateTime);
        request.setOtpCode(otp);
        request.setExpiresAt(expiresAt);

        try {
            // Gửi OTP qua email
            emailService.sendNotificationEmailDangKy(email, "Mã xác thực tài khoản! Không được chia sẻ mã này cho bất cứ ai",
                    "Mã OTP của bạn là: " + otp + ".\n Mã này tồn tại trong vòng 5 phút.");

            // Lưu VerificationRequest vào cơ sở dữ liệu nếu gửi email thành công
            verificationRequestRepository.save(request);

        } catch (Exception e) {
            // Nếu có lỗi trong quá trình gửi email hoặc lưu dữ liệu, rollback
            throw new RuntimeException("Đã xảy ra lỗi khi tạo yêu cầu xác thực: " + e.getMessage());
        }

        // Gửi OTP qua email
    }

    public String createVerificationRequestSMS(String fullname, String email, String password, String birthday, String phone) {
        String otp = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5); // OTP hết hạn sau 5 phút

        VerificationRequest request = new VerificationRequest();
        request.setFullname(fullname);
        request.setEmail(email);
        request.setPassword(password);
        request.setPhone(phone);
        // Chỉ định định dạng cho ngày giờ
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Chuyển đổi chuỗi birthday sang LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(birthday, formatter);
        request.setBirthday(dateTime);
        request.setOtpCode(otp);
        request.setExpiresAt(expiresAt);

        verificationRequestRepository.save(request);

        return otp;
    }

    public Optional<VerificationRequest> getVerificationRequestByEmail(String email) {
        return verificationRequestRepository.findByEmail(email);
    }

    public boolean verifyOTP(String email, String otp) {
        Optional<VerificationRequest> requestOptional = verificationRequestRepository.findByEmailAndOtpCode(email, otp);

        if (requestOptional.isEmpty()) {
            return false;
        }

        VerificationRequest request = requestOptional.get();

        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    // Hàm xóa bằng email
    @Transactional
    public void deleteVerificationRequestByEmail(String email) {
        verificationRequestRepository.deleteByEmail(email);
    }

    // Hàm xóa bằng ID
    public void deleteVerificationRequestById(Long id) {
        verificationRequestRepository.deleteById(id);
    }
}
