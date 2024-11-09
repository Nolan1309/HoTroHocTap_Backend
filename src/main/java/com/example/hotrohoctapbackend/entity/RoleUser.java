package com.example.hotrohoctapbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class RoleUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name")
    private String roleName;

//    @OneToMany(mappedBy = "role",
//            fetch = FetchType.LAZY
//            , cascade = {
//            CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH
//    })
//    private List<Account> accountList;

}