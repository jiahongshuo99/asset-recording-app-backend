package com.example.assetrecordingapp.model;

import javax.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_snapshot")
@Data
public class AssetSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "snapshot_time", nullable = false)
    private LocalDateTime snapshotTime;
    
    @Column(name = "amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "remark")
    private String remark;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
