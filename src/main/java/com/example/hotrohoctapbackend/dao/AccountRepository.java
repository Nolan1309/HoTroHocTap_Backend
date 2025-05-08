package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.DTO.AdminV3.AuthorAdmin;
import com.example.hotrohoctapbackend.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    public boolean existsByEmail(String email);

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Optional<Account> findByEmailOptional(@Param("email") String email);

    @Query("SELECT new com.example.hotrohoctapbackend.DTO.AdminV3.AuthorAdmin(a.id, a.fullname) " +
            "FROM Account a WHERE a.role.id = 3 AND a.isDeleted = false")
    List<AuthorAdmin> findAuthorsByRole();

    public Account findByEmail(String email);

    List<Account> findByIsDeletedTrue();

    @Query(value = "SELECT id, birthday, created_at, deleted_date, email, fullname, gender, google_id, " +
            "is_deleted, is_google_account, phone, updated_at, role_id " +
            "FROM account " +
            "WHERE role_id = 1 OR role_id = 3", nativeQuery = true)
    List<Object[]> findAccountsByRoles();

    @Query("SELECT a.email FROM Account a WHERE a.phone = :phone AND a.isDeleted = false")
    String findEmailByPhone(@Param("phone") String phone);


    // Kiểm tra số điện thoại đã tồn tại hay chưa
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.phone = :phone")
    boolean existsByPhone(@Param("phone") String phone);

    @Query(value = "SELECT id, birthday, created_at, deleted_date, email, fullname, gender, is_deleted, phone, updated_at, role_id FROM account WHERE is_deleted = 0 AND role_id = :roleId", nativeQuery = true)
    List<Object[]> findActiveAccountsByRoleId(@Param("roleId") int roleId);


    @Query(value = """
            SELECT 
                id, 
                birthday, 
                created_at, 
                deleted_date, 
                email, 
                fullname, 
                gender, 
                google_id, 
                image, 
                is_deleted, 
                is_google_account, 
                phone, 
                updated_at, 
                role_id 
            FROM 
                account 
            WHERE 
                is_deleted = 0 
                AND (:roleId IS NULL OR role_id = :roleId) 
                AND (
                    :searchTerm IS NULL 
                    OR fullname LIKE CONCAT('%', :searchTerm, '%') 
                    OR phone LIKE CONCAT('%', :searchTerm, '%') 
                    OR email LIKE CONCAT('%', :searchTerm, '%')
                )
            """,
            countQuery = """
                        SELECT COUNT(*) 
                        FROM 
                            account 
                        WHERE 
                            is_deleted = 0 
                            AND (:roleId IS NULL OR role_id = :roleId) 
                            AND (
                                :searchTerm IS NULL 
                                OR fullname LIKE CONCAT('%', :searchTerm, '%') 
                                OR phone LIKE CONCAT('%', :searchTerm, '%') 
                                OR email LIKE CONCAT('%', :searchTerm, '%')
                            )
                    """,
            nativeQuery = true)
    Page<Object[]> searchAccountsWithPagination(
            @Param("roleId") Integer roleId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    @Query(value = "SELECT id, birthday, created_at, deleted_date, email, fullname, gender, google_id, image, is_deleted, is_google_account, phone, updated_at, role_id FROM account WHERE is_deleted = 1",
            countQuery = "SELECT COUNT(*) FROM account WHERE is_deleted = 1",
            nativeQuery = true)
    Page<Object[]> findAllDeletedAccounts(Pageable pageable);


    @Query(
            value = """
                    SELECT id, birthday, created_at, deleted_date, email, fullname, gender, google_id, image, is_deleted, is_google_account, phone, updated_at, role_id
                    FROM account 
                    WHERE is_deleted = 1 AND fullname LIKE CONCAT('%', :fullName, '%')
                    ORDER BY created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM account 
                    WHERE is_deleted = 1 AND fullname LIKE CONCAT('%', :fullName, '%')
                    """,
            nativeQuery = true
    )
    Page<Object[]> searchAccountsByFullName(@Param("fullName") String fullName, Pageable pageable);

    @Query(
            value = """
                    SELECT a.id, a.birthday, a.created_at, a.deleted_date, a.email, a.fullname, a.gender, a.google_id, a.image, a.is_deleted, a.is_google_account, a.phone, a.updated_at, a.role_id 
                    FROM account a 
                    WHERE a.is_deleted = 1 
                    AND (:deletedDate IS NULL OR DATE(a.deleted_date) = :deletedDate)
                    ORDER BY a.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM account a 
                    WHERE a.is_deleted = 1 
                    AND (:deletedDate IS NULL OR DATE(a.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true
    )
    Page<Object[]> searchAccountsByDeletedDate(@Param("deletedDate") String deletedDate, Pageable pageable);

    @Query(
            value = """
                    SELECT a.id, a.birthday, a.created_at, a.deleted_date, a.email, a.fullname, a.gender, a.google_id, a.image, a.is_deleted, a.is_google_account, a.phone, a.updated_at, a.role_id 
                    FROM account a 
                    WHERE a.is_deleted = 1 
                    AND (:fullName IS NULL OR a.fullname LIKE CONCAT('%', :fullName, '%'))
                    AND (:deletedDate IS NULL OR DATE(a.deleted_date) = :deletedDate)
                    ORDER BY a.created_at DESC
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM account a 
                    WHERE a.is_deleted = 1 
                    AND (:fullName IS NULL OR a.fullname LIKE CONCAT('%', :fullName, '%'))
                    AND (:deletedDate IS NULL OR DATE(a.deleted_date) = :deletedDate)
                    """,
            nativeQuery = true
    )
    Page<Object[]> searchAccountsByFullNameAndDeletedDate(
            @Param("fullName") String fullName,
            @Param("deletedDate") String deletedDate,
            Pageable pageable
    );

    @Query(
            value = """
                    SELECT a.id, a.birthday, a.created_at, a.deleted_date, a.email, a.fullname, a.gender, a.google_id, a.image, a.is_deleted, a.is_google_account, a.phone, a.updated_at, a.role_id 
                    FROM account a 
                    WHERE a.is_deleted = 0 AND ( role_id = 3 OR role_id = 1 )
                    """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM account a 
                    WHERE a.is_deleted = 0 AND ( role_id = 3 OR role_id = 1 )
                    """,
            nativeQuery = true
    )
    List<Object[]> findAccountRestoreListAdminAndTeacher();

    @Query(value = """
            SELECT a.id, a.birthday, a.created_at, a.deleted_date, a.email, a.fullname, a.gender, a.google_id, a.image, a.is_deleted, a.is_google_account, a.phone, a.updated_at, a.role_id
            FROM account a
            JOIN enrolled_courses er on er.account_id = a.id
            WHERE  er.course_id = :courseId
            AND a.role_id = :roleId
            AND (:fullName IS NULL OR LOWER(a.fullname) LIKE LOWER(CONCAT('%', :fullName, '%')))
            AND (:enrollmentDate IS NULL OR DATE(er.enrollment_date) = :enrollmentDate)
            """,
            countQuery = """
                    SELECT COUNT(*) FROM account a
                    WHERE  er.course_id = :courseId
                    AND a.role_id = :roleId
                    AND (:fullName IS NULL OR LOWER(a.fullname) LIKE LOWER(CONCAT('%', :fullName, '%')))
                    AND (:enrollmentDate IS NULL OR DATE(er.enrollment_date) = :enrollmentDate)
                    """,
            nativeQuery = true)
    Page<Object[]> getAccountStudentByCourseIdAndRoleId(
            @Param("courseId") Integer courseId,
            @Param("roleId") Integer roleId,
            @Param("fullName") String fullName,
            @Param("enrollmentDate") String enrollmentDate,
            Pageable pageable);

    @Query("SELECT a FROM Account a WHERE "
            + "(LOWER(a.fullname) LIKE LOWER(CONCAT('%', :fullname, '%')) OR :fullname = '') "
            + "AND (:status IS NULL OR a.status = :status) "
            + "AND a.isDeleted = false")
    Page<Account> findAccountsByFilters(String fullname, Account.AccountStatus status, Pageable pageable);


}
