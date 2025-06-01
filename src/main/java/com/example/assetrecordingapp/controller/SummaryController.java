package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.pojo.AssetTrendData;
import com.example.assetrecordingapp.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @RequireLogin
    @GetMapping("/total")
    public HttpResponse<BigDecimal> getTotalAssets() {
        BigDecimal total = summaryService.getTotalAssets();
        return HttpResponse.success(total);
    }

    @GetMapping("/trend")
    public HttpResponse<AssetTrendData> getAssetTrend(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        AssetTrendData trendData = summaryService.getAssetTrend(startDate, endDate);
        return HttpResponse.success(trendData);
    }
}
