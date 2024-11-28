package com.example.hotrohoctapbackend.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public String from = "tranfc911@gmail.com";
    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String toEmail, String token) {
        String subject = "Reset your password";
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String body = "Click the following link to reset your password: " + resetUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    public void sendNotificationEmail(String toEmail, String title , String messageNo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(messageNo);
        message.setFrom(from);
        mailSender.send(message);
    }
    public void sendNotificationEmailDangKy(String toEmail, String title , String messageNo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(messageNo);
        message.setFrom(from);
        mailSender.send(message);
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
