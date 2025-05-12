package com.example.hotrohoctapbackend.dao;

import com.example.hotrohoctapbackend.entity.Ranking;
import com.example.hotrohoctapbackend.enums.PeriodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "ranking")
public interface RankingRepository extends JpaRepository<Ranking, Integer> {
    @Query("SELECT r FROM Ranking r " +
            "WHERE (:periodType IS NULL OR r.periodType = :periodType) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:startDate IS NULL OR r.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.createdAt <= :endDate) " +
            "AND (:ranking IS NULL OR r.ranking = :ranking) " +
            "AND (:accountName IS NULL OR r.account.fullname = :accountName) " +
            "ORDER BY r.createdAt DESC")
    Page<Ranking> findRankingsWithFilters(
            @Param("periodType") PeriodType periodType,
            @Param("status") Boolean status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("ranking") Integer ranking,
            @Param("accountName") String accountName,
            Pageable pageable
    );

    @Query("SELECT r FROM Ranking r " +
            "WHERE r.account.id = :accountId " +
            "AND r.periodType = :periodType " +
            "AND r.createdAt BETWEEN :startOfWeek AND :endOfWeek")
    List<Ranking> findByAccountAndPeriodTypeAndUpdatedAtBetween(
            @Param("accountId") Integer accountId,
            @Param("periodType") PeriodType periodType,
            @Param("startOfWeek") LocalDateTime startOfWeek,
            @Param("endOfWeek") LocalDateTime endOfWeek
    );

    @Query("SELECT r FROM Ranking r " +
            "WHERE r.account.id = :accountId " +
            "AND r.periodType = :periodType " +
            "AND r.createdAt = :startOfWeek")
    Ranking findByAccountAndPeriodTypeAndPeriodValue(
            @Param("accountId") Integer accountId,
            @Param("periodType") PeriodType periodType,
            @Param("startOfWeek") LocalDateTime startOfWeek
    );

    @Query("SELECT r FROM Ranking r WHERE r.periodType = :periodType AND r.createdAt >= :startOfDay AND r.createdAt < :endOfDay")
    List<Ranking> findByPeriodTypeAndDate(
            @Param("periodType") PeriodType periodType,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );
}
