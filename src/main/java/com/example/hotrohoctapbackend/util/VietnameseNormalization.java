package com.example.hotrohoctapbackend.util;

import java.util.HashMap;
import java.util.Map;

public class VietnameseNormalization {
    // Tạo bảng chuyển đổi các ký tự có dấu thành không dấu
    private static final Map<Character, Character> vietnameseMap = new HashMap<>();

    static {
        vietnameseMap.put('à', 'a');
        vietnameseMap.put('á', 'a');
        vietnameseMap.put('ạ', 'a');
        vietnameseMap.put('ả', 'a');
        vietnameseMap.put('ã', 'a');
        vietnameseMap.put('â', 'a');
        vietnameseMap.put('ầ', 'a');
        vietnameseMap.put('ấ', 'a');
        vietnameseMap.put('ẩ', 'a');
        vietnameseMap.put('ẫ', 'a');
        vietnameseMap.put('ă', 'a');
        vietnameseMap.put('ằ', 'a');
        vietnameseMap.put('ắ', 'a');
        vietnameseMap.put('ẳ', 'a');
        vietnameseMap.put('ẵ', 'a');
        vietnameseMap.put('è', 'e');
        vietnameseMap.put('é', 'e');
        vietnameseMap.put('ẹ', 'e');
        vietnameseMap.put('ẻ', 'e');
        vietnameseMap.put('ẽ', 'e');
        vietnameseMap.put('ê', 'e');
        vietnameseMap.put('ệ', 'e');
        vietnameseMap.put('ề', 'e');
        vietnameseMap.put('ế', 'e');
        vietnameseMap.put('ể', 'e');
        vietnameseMap.put('ễ', 'e');
        vietnameseMap.put('ì', 'i');
        vietnameseMap.put('í', 'i');
        vietnameseMap.put('ị', 'i');
        vietnameseMap.put('ỉ', 'i');
        vietnameseMap.put('ĩ', 'i');
        vietnameseMap.put('ò', 'o');
        vietnameseMap.put('ó', 'o');
        vietnameseMap.put('ọ', 'o');
        vietnameseMap.put('ỏ', 'o');
        vietnameseMap.put('õ', 'o');
        vietnameseMap.put('ô', 'o');
        vietnameseMap.put('ồ', 'o');
        vietnameseMap.put('ố', 'o');
        vietnameseMap.put('ổ', 'o');
        vietnameseMap.put('ỗ', 'o');
        vietnameseMap.put('ơ', 'o');
        vietnameseMap.put('ờ', 'o');
        vietnameseMap.put('ớ', 'o');
        vietnameseMap.put('ở', 'o');
        vietnameseMap.put('ỡ', 'o');
        vietnameseMap.put('ù', 'u');
        vietnameseMap.put('ú', 'u');
        vietnameseMap.put('ụ', 'u');
        vietnameseMap.put('ủ', 'u');
        vietnameseMap.put('ũ', 'u');
        vietnameseMap.put('ư', 'u');
        vietnameseMap.put('ừ', 'u');
        vietnameseMap.put('ứ', 'u');
        vietnameseMap.put('ử', 'u');
        vietnameseMap.put('ữ', 'u');
        vietnameseMap.put('ỳ', 'y');
        vietnameseMap.put('ý', 'y');
        vietnameseMap.put('ỵ', 'y');
        vietnameseMap.put('ỷ', 'y');
        vietnameseMap.put('ỹ', 'y');
        vietnameseMap.put('đ', 'd');

    }

    // Hàm chuẩn hóa tiếng Việt
    public static String normalizeVietnamese(String input) {
        if (input == null) {
            return "";
        }
        String lower = input.toString().toLowerCase();
        // Loại bỏ dấu tiếng Việt thủ công
        StringBuilder normalized = new StringBuilder();
        for (char c : lower.toCharArray()) {
            normalized.append(vietnameseMap.getOrDefault(c, c));
        }

        // Chuyển tất cả thành chữ thường
        String result = normalized.toString().toLowerCase();

        // Loại bỏ ký tự đặc biệt (giữ lại chữ cái và số)
        result = result.replaceAll("[^a-z0-9\\s]", "");

        // Loại bỏ khoảng trắng thừa ở đầu và cuối chuỗi
        result = result.trim();

        return result;
    }
}
