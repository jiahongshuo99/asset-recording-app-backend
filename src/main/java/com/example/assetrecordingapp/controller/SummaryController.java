package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/total")
    public HttpResponse<BigDecimal> getTotalAssets() {
        BigDecimal total = summaryService.getTotalAssets();
        return HttpResponse.success(total);
    }

    @GetMapping("/trend")
    public HttpResponse<List<Map<String, Object>>> getAssetTrend(
            @RequestParam(defaultValue = "30d") String range) {
        List<Map<String, Object>> trendData = summaryService.getAssetTrend(range);
        return HttpResponse.success(trendData);
    }
}
