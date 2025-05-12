package com.example.hotrohoctapbackend.util;

public enum TOPIC {
    // Quan trọng
    REGISTER,
    PASSWORD,

    // Ưu đãi
    VOUCHER,
    PAYMENT,

    // Hệ thống
    SYSTEM,
    GENERAL,

    // Nhắc học
    ENROLL_COURSE,
    LEARNING,
    CHAT;

    public static String getCategory(TOPIC topic) {
        switch (topic) {
            case REGISTER:
                return "ĐĂNG KÝ TÀI KHOẢN";
            case PASSWORD:
                return "ĐỔI MẬT KHẨU";
            case VOUCHER:
                return "MÃ GIẢM GIÁ";
            case PAYMENT:
                return "THANH TOÁN";
            case SYSTEM:
                return "HỆ THỐNG";
            case GENERAL:
                return "THÔNG BÁO CHUNG";
            case ENROLL_COURSE:
                return "ĐĂNG KÝ KHÓA HỌC";
            case LEARNING:
                return "HỌC TẬP";
            case CHAT:
                return "TRAO ĐỔI";
            default:
                return "Không xác định";
        }
    }
}
