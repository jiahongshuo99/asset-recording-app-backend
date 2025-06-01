package com.example.assetrecordingapp.repository;

import com.example.assetrecordingapp.model.DailyAssetSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyAssetSummaryRepository extends JpaRepository<DailyAssetSummary, Void> {
    
    @Query("SELECT d FROM DailyAssetSummary d WHERE d.userId = :userId AND d.date BETWEEN :startDate AND :endDate ORDER BY d.date")
    List<DailyAssetSummary> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
    
    @Query("SELECT d FROM DailyAssetSummary d WHERE d.userId = :userId AND d.date = :date")
    Optional<DailyAssetSummary> findByUserIdAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date);
    
    @Modifying
    @Query(value = "INSERT INTO daily_asset_summary (user_id, date, total) " +
            "VALUES (:userId, :date, :total) " +
            "ON DUPLICATE KEY UPDATE total = :total", 
            nativeQuery = true)
    void upsertDailySummary(
            @Param("userId") Long userId,
            @Param("date") LocalDate date,
            @Param("total") BigDecimal total);
}
