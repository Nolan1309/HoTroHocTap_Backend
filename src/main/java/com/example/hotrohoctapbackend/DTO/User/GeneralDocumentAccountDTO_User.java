package com.example.hotrohoctapbackend.DTO.User;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GeneralDocumentAccountDTO_User {
    private Integer id;
    private Integer accountId;
    private Integer generalDocumentId;
    private LocalDateTime dateDownload;
}
