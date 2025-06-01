package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.pojo.AssetTrendData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface SummaryService {
    BigDecimal getTotalAssets();
    AssetTrendData getAssetTrend(LocalDate startDate, LocalDate endDate);
}
