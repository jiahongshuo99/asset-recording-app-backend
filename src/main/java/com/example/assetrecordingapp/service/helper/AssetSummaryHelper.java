package com.example.assetrecordingapp.service.helper;

import com.example.assetrecordingapp.model.DailyAssetSummary;
import com.example.assetrecordingapp.pojo.AssetTrendData;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class AssetSummaryHelper {

    public AssetTrendData buildAssetTrend(LocalDate startDate, LocalDate endDate, List<DailyAssetSummary> summaryList) {
        // 将 summaryList 转成 Map<date, amount> 方便查找
        Map<LocalDate, BigDecimal> dateAmountMap = summaryList.stream()
                .collect(Collectors.toMap(DailyAssetSummary::getDate, DailyAssetSummary::getTotal));

        List<String> dates = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();

        BigDecimal lastAmount = BigDecimal.ZERO; // 假设初始值为 0

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            dates.add(date.toString());

            // 如果有该日记录，更新 lastAmount；否则沿用上一天
            if (dateAmountMap.containsKey(date)) {
                lastAmount = dateAmountMap.get(date);
            }
            amounts.add(lastAmount);

            date = date.plusDays(1);
        }

        return AssetTrendData.builder()
                .dateList(dates)
                .amountList(amounts)
                .build();
    }

}
