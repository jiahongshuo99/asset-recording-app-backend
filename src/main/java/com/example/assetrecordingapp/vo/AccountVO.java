package com.example.assetrecordingapp.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountVO {
    private Long id;
    private String name;
    private String type;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal currentAmount;
}
