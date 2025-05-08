package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.DocumentRelateUserDTO;
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
            "ORDER BY gd.created_at DESC", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByDateDesc();

    //    Lay tai lieu co view giam dan
    @Query(value = "SELECT " +
            "gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at " +
            "ORDER BY gd.view DESC", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByViewDesc();

    //  Lay tai lieu co download giam dan
    @Query(value = "SELECT " +
            "gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at " +
            "ORDER BY download_count DESC", nativeQuery = true)
    public List<Object[]> findAllWithDownloadCountOrderedByDownloadCountDesc();

    @Query(value = "SELECT " +
            "gd.id AS documentId, " +
            "gd.title AS documentTitle, " +
            "gd.image AS image_url, " +
            "gd.url AS url, " +
            "gd.view AS view, " +
            "gd.created_at AS created_at, " +
            "COUNT(gda.id) AS download_count, " +
            "gd.id_category " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at",
            countQuery = "SELECT COUNT(DISTINCT gd.id) " +
                    "FROM general_documents gd " +
                    "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id",
            nativeQuery = true)
    Page<Object[]> findDocumentsAll(Pageable pageable);


    @Query(value = "SELECT   \n" +
            "    gd.id AS documentId, \n" +
            "    gd.title AS documentTitle, \n" +
            "    gd.image AS image_url, \n" +
            "    gd.url AS url, \n" +
            "    gd.view AS view, \n" +
            "    gd.created_at AS created_at, \n" +
            "    COALESCE(COUNT(gda.id), 0) AS download_count, \n" +
            "    ca.name \n" +
            "FROM general_documents gd\n" +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id \n" +
            "INNER JOIN categories ca ON ca.id_category = gd.id_category\n" +
            "GROUP BY \n" +
            "    gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, ca.name \n" +
            "ORDER BY download_count DESC, gd.created_at DESC \n" +
            "LIMIT 100;\n",
            nativeQuery = true)
    List<Object[]> findTop100Documents();


    @Query(value = "SELECT\n" +
            "            gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at, COUNT(gda.id) AS download_count \n" +
            "            FROM general_documents gd \n" +
            "            LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id \n" +
            "            GROUP BY gd.id, gd.title, gd.image, gd.url, gd.view, gd.created_at\n" +
            "            ORDER BY gd.view DESC LIMIT 6", nativeQuery = true)
    public List<Object[]> findAllWithViewDesc();

//    @Query(value = "SELECT * FROM general_documents WHERE id = :id", nativeQuery = true)
//    Object[] getDocumentByID(@Param("id") int id);


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

    @Query(value = "SELECT * FROM general_documents", nativeQuery = true)
    Page<Object> GetAll(Pageable pageable);


    @Query(value = """
            SELECT\s
                                   gd.id AS document_id,
                                   gd.title AS document_title,\s
                                   gd.description AS document_description,\s
                                   gd.url AS document_url,
                                   gd.is_deleted AS deleted,
                                   c1.name AS category_level_1,\s
                                   c2.name AS category_level_2,\s
                                   c3.name AS category_level_3,
                                   gd.id_category,
                                   gd.view,
                                   gd.created_at,
                                   gd.status
                               FROM\s
                                   general_documents gd\s
                               LEFT JOIN categories c3 ON gd.id_category = c3.id_category
                               LEFT JOIN categories c2 ON c2.id_category = c3.parent_id
                               LEFT JOIN categories c1 ON c1.id_category = c2.parent_id
                               where gd.is_deleted = 0
            """,
            countQuery = """
                    SELECT 
                        COUNT(*)
                    FROM 
                        general_documents gd 
                    LEFT JOIN categories c3 ON gd.id_category = c3.id_category
                    LEFT JOIN categories c2 ON c2.id_category = c3.parent_id
                    LEFT JOIN categories c1 ON c1.id_category = c2.parent_id
                     where gd.is_deleted = 0
                    """,
            nativeQuery = true)
    Page<Object[]> findDocumentsWithCategories(Pageable pageable);

    @Query(value = """
            SELECT 
                gd.id AS document_id,
                gd.title AS document_title,
                gd.description AS document_description,
                gd.url AS document_url,
                gd.is_deleted AS deleted,
                c1.name AS category_level_1,
                c2.name AS category_level_2,
                c3.name AS category_level_3,
                gd.id_category,
                gd.view,
                gd.created_at,
                gd.status
            FROM 
                general_documents gd
            LEFT JOIN categories c3 ON gd.id_category = c3.id_category
            LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
            LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
            WHERE 
                gd.is_deleted = 0
                AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                AND (:searchTerm IS NULL OR gd.title LIKE CONCAT('%', :searchTerm, '%'))
            """,
            countQuery = """
                    SELECT 
                        COUNT(*)
                    FROM 
                        general_documents gd
                    LEFT JOIN categories c3 ON gd.id_category = c3.id_category
                    LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
                    LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
                    WHERE 
                        gd.is_deleted = 0
                        AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                        AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                        AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                        AND (:searchTerm IS NULL OR gd.title LIKE CONCAT('%', :searchTerm, '%'))
                    """,
            nativeQuery = true)
    Page<Object[]> findDocumentsWithCategoriesSearch(
            @Param("categoryId1") Integer categoryId1,
            @Param("categoryId2") Integer categoryId2,
            @Param("categoryId3") Integer categoryId3,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );


    @Query(value = "SELECT \n" +
            "    g.id AS id,\n" +
            "    g.id_category AS idCategoryLevel3,\n" +
            "    g.url AS url,\n" +
            "    g.title AS title,\n" +
            "    g.description AS description,\n" +
            "    c3.id_category AS idCategoryLevel1,\n" +
            "    c2.id_category AS idCategoryLevel2,\n" +
            "    g.image as image \n" +
            "FROM \n" +
            "    general_documents g\n" +
            "INNER JOIN \n" +
            "    categories c1 ON g.id_category = c1.id_category AND c1.level = 3\n" +
            "INNER JOIN \n" +
            "    categories c2 ON c1.parent_id = c2.id_category AND c2.level = 2\n" +
            "INNER JOIN \n" +
            "    categories c3 ON c2.parent_id = c3.id_category AND c3.level = 1\n" +
            "WHERE \n" +
            "    g.id = :id;\n", nativeQuery = true)
    List<Object[]> findDocumentDetailsById(@Param("id") int id);

    // Native query để tìm kiếm theo title và category với phân trang
    @Query(value = "SELECT gd.id,gd.created_at, gd.description, gd.image, gd.title, gd.updated_at,gd.url, gd.view, gd.id_category , c.name AS category_name " +
            "FROM general_documents gd " +
            "LEFT JOIN categories c ON gd.id_category = c.id_category " +
            "WHERE gd.title LIKE %:keyword% " +
            "OR c.name LIKE %:keyword%",
            countQuery = "SELECT COUNT(*) FROM general_documents gd " +
                    "LEFT JOIN categories c ON gd.id_category = c.id_category " +
                    "WHERE gd.title LIKE %:keyword% " +
                    "OR c.name LIKE %:keyword%",
            nativeQuery = true)
    Page<Object[]> searchDocumentsByTitleOrCategory(@Param("keyword") String keyword, Pageable pageable);


    @Query(value = "SELECT gd.id, gd.title, COUNT(gda.id) AS total_downloads, gd.view AS total_views " +
            "FROM general_documents gd " +
            "LEFT JOIN general_document_acount gda ON gd.id = gda.generaldocument_id " +
            "WHERE gd.id_category = :categoryId " +
            "GROUP BY gd.id, gd.title, gd.view " +
            "ORDER BY gd.title", nativeQuery = true)
    List<Object[]> findDocumentSummariesByCategoryId(@Param("categoryId") Long categoryId);


    @Query(value = """
                SELECT 
                    d.id AS documentId, 
                    d.title AS title, 
                    dc.date_download AS dateDownload, 
                    d.url AS url
                FROM 
                    general_documents d
                INNER JOIN 
                    general_document_acount dc 
                ON 
                    d.id = dc.generaldocument_id
                WHERE 
                    dc.account_id = :accountId
                ORDER BY 
                    dc.date_download DESC
                LIMIT :offset, :pageSize
            """, nativeQuery = true)
    List<Object[]> findDocumentsByAccountIdUser(
            @Param("accountId") Long accountId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    @Query(value = """
                SELECT COUNT(*)
                FROM 
                    general_documents d
                INNER JOIN 
                    general_document_acount dc 
                ON 
                    d.id = dc.generaldocument_id
                WHERE 
                    dc.account_id = :accountId
            """, nativeQuery = true)
    long countDocumentsByAccountIdUser(@Param("accountId") Long accountId);


    @Query(value = """
            SELECT d.id, d.created_at, d.description, d.image, d.title, d.updated_at, d.url, d.view, d.id_category, d.deleted_date, d.is_deleted, d.status
            FROM general_documents d
            LEFT JOIN categories c3 ON d.id_category = c3.id_category
            LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
            LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
            WHERE d.is_deleted = 1
            AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
            AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
            AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
            AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
            AND (:deletedDate IS NULL OR DATE(d.deleted_date) = :deletedDate)
            """,
            countQuery = """
                    SELECT COUNT(*) FROM general_documents d
                    LEFT JOIN categories c3 ON d.id_category = c3.id_category
                    LEFT JOIN categories c2 ON c3.parent_id = c2.id_category
                    LEFT JOIN categories c1 ON c2.parent_id = c1.id_category
                    WHERE d.is_deleted = 1
                    AND (:categoryId1 IS NULL OR c1.id_category = :categoryId1)
                    AND (:categoryId2 IS NULL OR c2.id_category = :categoryId2)
                    AND (:categoryId3 IS NULL OR c3.id_category = :categoryId3)
                    AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%')))
                    AND (:deletedDate IS NULL OR DATE(d.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true)
    Page<Object[]> findGeneralDocumentsBy(
            @Param("categoryId1") Integer categoryId1,
            @Param("categoryId2") Integer categoryId2,
            @Param("categoryId3") Integer categoryId3,
            @Param("title") String title,
            @Param("deletedDate") String deletedDate,
            Pageable pageable);

    @Query("SELECT g FROM GeneralDocument g WHERE "
            + "(LOWER(g.title) LIKE LOWER(CONCAT('%', :title, '%')) OR :title = '') "
            + "AND (g.status = :status OR :status = '') "
            + "AND g.isDeleted = false ")
    Page<GeneralDocument> findGeneralDocumentsByFilters(
            String title,
            String status,
            Pageable pageable
    );

    // Tài liệu phổ biến (lượt xem cao nhất), lọc theo tên, danh mục và định dạng
    @Query("SELECT gd FROM GeneralDocument gd WHERE " +
            "gd.isDeleted = false AND " +
            "gd.status = 'ACTIVE' AND " +
            "(:title IS NULL OR gd.title LIKE %:title%) AND " +
            "(:categoryId IS NULL OR gd.category.id = :categoryId) AND " +
            "(:format IS NULL OR gd.format = :format) " +
            "ORDER BY gd.view DESC")
    Page<GeneralDocument> findMostPopularDocuments(String title, Integer categoryId, String format, Pageable pageable);

    // Tài liệu mới nhất (dựa trên ngày tạo), lọc theo tên, danh mục và định dạng
    @Query("SELECT gd FROM GeneralDocument gd WHERE " +
            "gd.isDeleted = false AND " +
            "gd.status = 'ACTIVE' AND " +
            "(:title IS NULL OR gd.title LIKE %:title%) AND " +
            "(:categoryId IS NULL OR gd.category.id = :categoryId) AND " +
            "(:format IS NULL OR gd.format = :format) " +
            "ORDER BY gd.createdAt DESC")
    Page<GeneralDocument> findLatestDocuments(String title, Integer categoryId, String format, Pageable pageable);

    @Query("SELECT gd FROM GeneralDocument gd " +
            "LEFT JOIN gd.generalDocumentAcounts gda " +
            "WHERE gd.isDeleted = false AND " +
            "gd.status = 'ACTIVE' AND " +
            "(:title IS NULL OR gd.title LIKE %:title%) AND " +
            "(:categoryId IS NULL OR gd.category.id = :categoryId) AND " +
            "(:format IS NULL OR gd.format = :format) AND " +
            "(gd.view > :minView OR COUNT(gda) > :minDownload) " + // Lọc theo lượt xem và lượt tải
            "GROUP BY gd.id " +
            "ORDER BY gd.view DESC, COUNT(gda) DESC")
    Page<GeneralDocument> findRecommendedDocuments(String title, Integer categoryId, String format, int minView, int minDownload, Pageable pageable);
}

