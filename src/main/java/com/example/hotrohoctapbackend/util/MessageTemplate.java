package com.example.hotrohoctapbackend.util;

public class MessageTemplate {
    public enum Message {
        REGISTER("Chúc mừng bạn đã đăng ký thành công! Xin chào %s."),
        ENROLL_COURSE("Bạn đã đăng ký khóa học %s thành công! Hãy bắt đầu học ngay."),
        LEARNING("Nhắc nhở: Hôm nay bạn có lịch học môn %s. Đừng quên tham gia."),
        PASSWORD("Mật khẩu của bạn đã được thay đổi thành công."),
        PAYMENT("Thanh toán thành công cho đơn hàng %s. Cảm ơn bạn đã mua hàng."),
        CHAT("Có tin nhắn mới từ %s. Hãy kiểm tra ngay!"),
        SYSTEM("Thông báo hệ thống: %s."),
        GENERAL("Thông báo: %s.");

        private final String template;

        // Constructor
        Message(String template) {
            this.template = template;
        }

        // Getter để lấy template thông báo
        public String getTemplate() {
            return template;
        }
    }

    public static String getMessage(Message message, String... params) {
        return String.format(message.getTemplate(), (Object[]) params);
    }
}
