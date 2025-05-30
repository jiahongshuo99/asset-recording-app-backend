package com.example.assetrecordingapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SummaryService {
    BigDecimal getTotalAssets();
    List<Map<String, Object>> getAssetTrend(String range);
}
