package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.GeneralDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

import java.util.List;

@RepositoryRestResource(path = "generaldocuments")
public interface GeneralDocumentRepository extends JpaRepository<GeneralDocument, Integer> {
    @Query(value = """
        SELECT 
            gd.id AS document_id,
            gd.title AS document_title, 
            gd.description AS document_description, 
            gd.url AS document_url, 
            c1.name AS category_level_1, 
            c2.name AS category_level_2, 
            c3.name AS category_level_3
        FROM 
            general_documents gd 
        LEFT JOIN categories c3 ON gd.id_category = c3.id_category
        LEFT JOIN categories c2 ON c2.id_category = c3.parent_id
        LEFT JOIN categories c1 ON c1.id_category = c2.parent_id
        """, nativeQuery = true)
    List<Object[]> findDocumentsWithCategories();


    public GeneralDocument findById(int id);

}
