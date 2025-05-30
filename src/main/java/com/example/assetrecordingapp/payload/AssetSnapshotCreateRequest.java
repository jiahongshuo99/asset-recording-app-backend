package com.example.assetrecordingapp.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssetSnapshotCreateRequest {
    private Long accountId;
    private String amount;
}
