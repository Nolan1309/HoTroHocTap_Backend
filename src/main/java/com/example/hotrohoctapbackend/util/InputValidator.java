package com.example.hotrohoctapbackend.util;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_REGEX = "^\\d{10,15}$"; // Tùy chỉnh độ dài số điện thoại (10-15 ký tự)

    public static boolean isEmail(String input) {
        return Pattern.matches(EMAIL_REGEX, input);
    }

    public static boolean isPhone(String input) {
        return Pattern.matches(PHONE_REGEX, input);
    }
}
