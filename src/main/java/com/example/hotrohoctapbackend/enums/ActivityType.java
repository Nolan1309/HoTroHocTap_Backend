package com.example.hotrohoctapbackend.enums;

public enum ActivityType {
    LOGIN,             // Đăng nhập
    LOGOUT,            // Đăng xuất
    CHECK_IN,          // Kiểm tra vào lớp
    VIEW_LESSON,       // Xem bài giảng
    START_EXAM,        // Bắt đầu thi
    SUBMIT_EXAM,       // Nộp bài thi
    COMMENT,           // Bình luận
    LIKE,              // Thích bài viết
    ADD_COURSE_TO_CART, // Thêm khóa học vào giỏ hàng
    REMOVE_COURSE_FROM_CART, // Xóa khóa học khỏi giỏ hàng
    PURCHASE_COURSE,   // Mua khóa học
    COMPLETE_COURSE,   // Hoàn thành khóa học
    MESSAGE_INSTRUCTOR, // Nhắn tin cho giảng viên
    JOIN_COMMUNITY_GROUP, // Tham gia nhóm cộng đồng
    SEND_MESSAGE_IN_COMMUNITY, // Gửi tin nhắn trong cộng đồng
    WATCH_VIDEO         // Xem video
}
