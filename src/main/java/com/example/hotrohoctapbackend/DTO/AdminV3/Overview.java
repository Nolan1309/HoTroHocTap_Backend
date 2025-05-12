package com.example.hotrohoctapbackend.DTO.AdminV3;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Overview {

    private Integer accountId;
    private String accountName;
    private String email;
    private Integer totalPoints;
    private Integer dayStreak;
    private Integer countCourse;
    private Integer countDocument;
    private BigDecimal balanceWallet;
    private Integer walletId;

}
