package com.example.hotrohoctapbackend.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public String from = "tranfc911@gmail.com";
    @Autowired
    private JavaMailSender mailSender;

    @Value("${allowed.origins}")
    private String allowedOrigins;

    public void sendResetPasswordEmail(String toEmail, String token) {
        String subject = "Reset your password";
        String resetUrl = allowedOrigins + "/reset-password?token=" + token;
        String body = "Click the following link to reset your password: " + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendNotificationEmail(String toEmail, String title, String messageNo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(messageNo);
        message.setFrom(from);
        mailSender.send(message);
    }

    public void sendNotificationEmailDangKy(String toEmail, String title, String messageNo) {
        // Kiểm tra định dạng email hợp lệ
        if (toEmail == null || !isValidEmail(toEmail)) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(messageNo);

        // Cấu hình từ email (cần xác định từ đâu email được gửi)
        message.setFrom("noreply@example.com"); // Đảm bảo bạn có một địa chỉ email hợp lệ từ SMTP server

        try {
            // Gửi email
            mailSender.send(message);
        } catch (Exception e) {
            // Xử lý lỗi khi gửi email
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage());
        }
    }

    // Kiểm tra định dạng email hợp lệ
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }


    public void sendOrderConfirmationEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(from); // Thay bằng địa chỉ email của bạn

        mailSender.send(message);
    }
}
