package com.example.hotrohoctapbackend.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Category category;

    @Column(name = "level")
    private int level;

//    @OneToMany(mappedBy = "category",
//            fetch = FetchType.LAZY
//            , cascade = {
//            CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH
//    })
//    private List<Course> courseList;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JsonManagedReference
    private List<Category> subCategories;

//    @OneToMany(mappedBy = "category",
//            fetch = FetchType.EAGER
//            , cascade = {
//            CascadeType.DETACH, CascadeType.MERGE,
//            CascadeType.PERSIST, CascadeType.REFRESH
//    })
//    private List<GeneralDocument> generalDocumentList;
}
