package com.example.hotrohoctapbackend.DTO;

import lombok.Data;

@Data
public class CourseInfoDetailDTO_Test_User {
    private int test_id;
    private String test_title;
    private String test_type;

    public CourseInfoDetailDTO_Test_User(int test_id, String test_title, String test_type) {
        this.test_id = test_id;
        this.test_title = test_title;
        this.test_type = test_type;
    }
}
