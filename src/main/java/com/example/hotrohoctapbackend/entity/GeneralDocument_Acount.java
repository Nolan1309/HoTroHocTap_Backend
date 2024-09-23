package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "generalDocument_acount")
public class GeneralDocument_Acount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "generaldocument_id")
    private GeneralDocument generalDocument;

}
