package com.example.hotrohoctapbackend.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


@Data
@Entity
@Table(name = "passwordResetTokens")
public class PasswordResetToken {
    private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    @ManyToOne()
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "expiryDate", nullable = false)
    private Date expiryDate;

    public PasswordResetToken() {
    }
    public PasswordResetToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }
    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }
    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}