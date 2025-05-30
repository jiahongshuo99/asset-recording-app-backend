package com.example.assetrecordingapp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import java.time.LocalDate;

@Embeddable
@Data
public class DailyAssetSummaryId {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "date", nullable = false)
    private LocalDate date;
}
