package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.context.UserContext;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetSnapshotServiceImpl implements AssetSnapshotService {

    @Resource
    private AssetSnapshotRepository assetSnapshotRepository;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    private static final String LATEST_ASSETS_KEY = "user:{userId}:latest_assets";


    @Override
    @Transactional
    public AssetSnapshot addSnapshot(AssetSnapshot snapshot) {
        // Set creation timestamp
        snapshot.setCreatedAt(LocalDateTime.now());
        
        // Save to database
        AssetSnapshot savedSnapshot = assetSnapshotRepository.save(snapshot);
        
        // Update Redis cache with latest amount
        // todo updateLatestAssetCache(savedSnapshot);
        
        return savedSnapshot;
    }

    @Override
    public List<AssetSnapshot> getSnapshotsByAccount(Long accountId) {
        return assetSnapshotRepository.findByAccountIdOrderBySnapshotTimeDesc(accountId);
    }

    private void updateLatestAssetCache(AssetSnapshot snapshot) {
        Long userId = UserContext.getCurrentUserId();
        String redisKey = LATEST_ASSETS_KEY.replace("{userId}", userId.toString());
        
        HashOperations<String, String, BigDecimal> hashOps = redisTemplate.opsForHash();
        hashOps.put(redisKey, "account:" + snapshot.getAccountId(), snapshot.getAmount());
    }
}
