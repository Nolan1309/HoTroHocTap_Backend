package com.example.hotrohoctapbackend.DTO.Admin;

import lombok.Data;
import java.util.Date;

@Data
public class AdminTestGetDTO {
    private Integer id;
    private String title;
    private int totalQuestion;
    private Date createdAt;
    private boolean deleted;

    // You can add a constructor if needed
    public AdminTestGetDTO(Integer id, String title, int totalQuestion, Date createdAt ,boolean deleted) {
        this.id = id;
        this.title = title;
        this.totalQuestion = totalQuestion;
        this.createdAt = createdAt;
        this.deleted = deleted;
    }
}
