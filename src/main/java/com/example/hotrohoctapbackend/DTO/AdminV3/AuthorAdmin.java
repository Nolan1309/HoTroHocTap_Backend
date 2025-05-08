package com.example.hotrohoctapbackend.DTO.AdminV3;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthorAdmin {
    private Integer id;
    private String name;

    public AuthorAdmin(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
