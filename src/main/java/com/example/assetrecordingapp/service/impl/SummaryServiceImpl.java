package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final AccountRepository accountRepository;
    private final AssetSnapshotRepository assetSnapshotRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LATEST_ASSETS_KEY = "user:{userId}:latest_assets";
    private static final String TREND_KEY = "user:{userId}:trend";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    public SummaryServiceImpl(AccountRepository accountRepository,
                            AssetSnapshotRepository assetSnapshotRepository,
                            RedisTemplate<String, Object> redisTemplate) {
        this.accountRepository = accountRepository;
        this.assetSnapshotRepository = assetSnapshotRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public BigDecimal getTotalAssets() {
        // Get current user ID from security context
        Long userId = getCurrentUserId();
        String redisKey = LATEST_ASSETS_KEY.replace("{userId}", userId.toString());
        
        HashOperations<String, String, BigDecimal> hashOps = redisTemplate.opsForHash();
        Map<String, BigDecimal> latestAssets = hashOps.entries(redisKey);
        
        if (latestAssets.isEmpty()) {
            // If cache is empty, calculate from DB and update cache
            latestAssets = calculateLatestAssets(userId);
            if (!latestAssets.isEmpty()) {
                hashOps.putAll(redisKey, latestAssets);
            }
        }
        
        return latestAssets.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Map<String, Object>> getAssetTrend(String range) {
        Long userId = getCurrentUserId();
        String redisKey = TREND_KEY.replace("{userId}", userId.toString());
        
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        Set<Object> trendData = zSetOps.range(redisKey, 0, -1);
        
        if (trendData == null || trendData.isEmpty()) {
            // If cache is empty, calculate from DB and update cache
            return calculateTrendFromDB(userId, range);
        }
        
        return trendData.stream()
                .map(item -> {
                    Map<String, Object> entry = new HashMap<>();
                    String[] parts = item.toString().split(":");
                    entry.put("date", parts[0]);
                    entry.put("total", new BigDecimal(parts[1]));
                    return entry;
                })
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> calculateLatestAssets(Long userId) {
        // Implementation to get latest assets from DB
        return Collections.emptyMap();
    }

    private List<Map<String, Object>> calculateTrendFromDB(Long userId, String range) {
        // Implementation to calculate trend from DB
        return Collections.emptyList();
    }

    private Long getCurrentUserId() {
        // Implementation to get current user ID from security context
        return 1L; // Placeholder - replace with actual user ID from security context
    }
}
