package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.GeneralDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "generaldocuments")
public interface GeneralDocumentRepository extends JpaRepository<GeneralDocument, Integer> {

    //Lấy thông tin tài liệu theo ngày giảm dần
    @Query(value = "SELECT " +
            "gd.id, " +
            "gd.title, " +
            "gd.image, " +
            "gd.url, " +
            "gd.view, " +
            "gd.created_at, " +
            "COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at " +
            "ORDER BY gd.created_at DESC LIMIT 3", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByDateDesc();

    //    Lay tai lieu co view giam dan
    @Query(value = "SELECT " +
            "gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at " +
            "ORDER BY gd.view DESC LIMIT 3", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByViewDesc();

    //  Lay tai lieu co download giam dan
    @Query(value = "SELECT " +
            "gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at " +
            "ORDER BY download_count DESC LIMIT 3", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByDownloadCountDesc();

    @Query(value = "SELECT " +
            "gd.id AS documentId, " +
            "gd.title AS documentTitle, " +
            "gd.image AS image_url, " +
            "gd.url AS url, " +
            "gd.view AS view, " +
            "gd.created_at AS created_at, " +
            "COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at",
            countQuery = "SELECT COUNT(gd.id) FROM general_documents gd LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id",
            nativeQuery = true)
    Page<Object[]> findDocumentsAll(Pageable pageable);


    @Query(value = "SELECT\n" +
            "            gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count \n" +
            "            FROM general_documents gd \n" +
            "            LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id \n" +
            "            GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at\n" +
            "            ORDER BY gd.view DESC LIMIT 6", nativeQuery = true)
    public List<Object[]> findAllWithViewDesc();

    @Query(value = "SELECT * FROM general_documents WHERE id = :id", nativeQuery = true)
    Object[] getDocumentByID(@Param("id") int id);


    @Query(value = "SELECT d.id AS documentId, " +
            "d.title AS documentTitle, " +
            "d.image AS image_url, " +
            "d.url AS url, " +
            "d.view AS view, " +
            "d.created_at AS created_at, " +
            "COUNT(ga.id) AS downloadCount " +
            "FROM general_documents d " +
            "JOIN categories c ON d.id_category = c.id_category " +
            "LEFT JOIN general_document_acount ga ON d.id = ga.generaldocument_id " +
            "WHERE c.id_category IN ( " +
            "  SELECT id_category " +
            "  FROM categories " +
            "  WHERE id_category = :categoryId " +
            "  OR parent_id = :categoryId " +
            "  OR parent_id IN ( " +
            "    SELECT id_category " +
            "    FROM categories " +
            "    WHERE parent_id = :categoryId " +
            "  ) " +
            ") " +
            "GROUP BY d.id",
            countQuery = "SELECT COUNT(d.id) " +
                    "FROM general_documents d " +
                    "JOIN categories c ON d.id_category = c.id_category " +
                    "WHERE c.id_category IN ( " +
                    "  SELECT id_category " +
                    "  FROM categories " +
                    "  WHERE id_category = :categoryId " +
                    "  OR parent_id = :categoryId " +
                    "  OR parent_id IN ( " +
                    "    SELECT id_category " +
                    "    FROM categories " +
                    "    WHERE parent_id = :categoryId " +
                    "  ) " +
                    ")",
            nativeQuery = true)
    Page<Object[]> findDocumentsByCategory(@Param("categoryId") Long categoryId, Pageable pageable);


    @Query(value = "SELECT " +
            "    gd.id AS document_id, " +
            "    gd.title AS document_title, " +
            "    gd.image AS document_image, " +
            "    gd.url AS document_url, " +
            "    gd.view AS document_view, " +
            "    gd.created_at AS document_created_at, " +
            "    COUNT(gda.id) AS download_count " +
            "FROM " +
            "    general_documents gd " +
            "LEFT JOIN " +
            "    general_document_acount gda " +
            "ON " +
            "    gd.id = gda.generaldocument_id " +
            "WHERE " +
            "    gd.title LIKE %:title% " +
            "GROUP BY " +
            "    gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at",
            countQuery = "SELECT COUNT(*) " +
                    "FROM general_documents gd " +
                    "LEFT JOIN general_document_acount gda " +
                    "ON gd.id = gda.generaldocument_id " +
                    "WHERE gd.title LIKE %:title%",
            nativeQuery = true)
    Page<Object[]> findDocumentsWithTitle(@Param("title") String title, Pageable pageable);


}

