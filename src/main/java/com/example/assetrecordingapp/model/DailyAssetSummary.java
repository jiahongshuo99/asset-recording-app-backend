package com.example.assetrecordingapp.model;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "daily_asset_summary")
@Data
public class DailyAssetSummary {
    @EmbeddedId
    private DailyAssetSummaryId id;
    
    @Column(name = "total", nullable = false, precision = 20, scale = 2)
    private BigDecimal total;
}
