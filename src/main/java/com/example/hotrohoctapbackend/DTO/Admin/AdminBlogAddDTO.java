package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminBlogAddDTO {
    private int id;
    private String title;
    private String content;
    private Boolean status;
    private String image;
    private int cat_id;
    private int author_id;
}
