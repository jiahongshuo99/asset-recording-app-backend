package com.example.assetrecordingapp.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_asset_summary")
@IdClass(DailyAssetSummaryId.class)
public class DailyAssetSummary {
    
    @Id
    @Column(name = "user_id")
    private Long userId;
    
    @Id
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "total", precision = 20, scale = 2)
    private BigDecimal total;
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}

class DailyAssetSummaryId implements java.io.Serializable {
    private Long userId;
    private LocalDate date;
    
    // Default constructor
    public DailyAssetSummaryId() {}
    
    // Constructor
    public DailyAssetSummaryId(Long userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
