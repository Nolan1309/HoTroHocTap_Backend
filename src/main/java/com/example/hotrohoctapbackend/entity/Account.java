package com.example.hotrohoctapbackend.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table(name = "account")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @Transient
//    @JsonProperty("role_id")
//    private int roleId;


    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleUser role;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<User_Notification> userNotificationList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<ActivityLogs> activityLogsList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Banner> bannerList;

    @OneToMany(mappedBy = "author",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Blog> blogList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Comment> commentList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Enrolled_Courses> enrolledCoursesList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Favorites> favoritesList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<LearningResult> learningResultList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Payment> paymentList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Ranking> rankingList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<Review> reviewList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<TestResult> testResultList;

    @OneToMany(mappedBy = "account",
            fetch = FetchType.LAZY
            , cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    private List<User_VipSubscription> userVipSubscriptionList;



//    Method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }
    @Override
    public String getUsername() {
        return email;
    }
}