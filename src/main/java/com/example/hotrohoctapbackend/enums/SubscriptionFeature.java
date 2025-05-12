package com.example.hotrohoctapbackend.enums;

public enum SubscriptionFeature {
    ACCESS_TO_COURSES("Truy cập không giới hạn các bài giảng video"),
    ACCESS_TO_EXCLUSIVE_CONTENT("Truy cập nội dung độc quyền"),
    DISCOUNT_ON_PRODUCTS("Giảm giá các sản phẩm khác"),
    CERTIFICATION("Chứng chỉ hoàn thành khóa học"),
    PRIVATE_SUPPORT("Hỗ trợ riêng tư qua email"),
    PREMIUM_SUPPORT("Hỗ trợ ưu tiên 24/7"),
    ACCESS_TO_TESTS("Truy cập các bài kiểm tra và đề thi thử"),
    MENTOR_SESSION("Mentor cá nhân"),
    PERSONALIZED_LEARNING("Lộ trình học tập cá nhân hóa"),
    LIVE_QA_SESSIONS("Buổi hỏi đáp trực tiếp hàng tuần"),
    COMMUNITY_ACCESS("Quyền truy cập vào cộng đồng học tập riêng"),
    EXCLUSIVE_EVENTS("Tham gia sự kiện độc quyền"),
    NEW_COURSES_FREE("Nhận các khóa học mới miễn phí"),
    ADVANCED_EXERCISES("Bài tập nâng cao"),
    SUPPLEMENTARY_MATERIALS("Tài liệu học tập bổ sung");

    private final String description;

    SubscriptionFeature(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
