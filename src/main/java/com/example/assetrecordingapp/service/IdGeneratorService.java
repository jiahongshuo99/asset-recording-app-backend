package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.repository.UserRepository;
import com.example.assetrecordingapp.service.manager.DistributedLockManager;
import com.example.assetrecordingapp.service.manager.LockKeyManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IdGeneratorService {
    private static final String ID_COUNTER_KEY = "user_id_counter";
    private static final long BASE_ID = 1000001;
    private static final long ID_INCREMENT = 2;

    @Resource
    private DistributedLockManager lockManager;

    @Resource
    private LockKeyManager lockKeyManager;

    @Resource
    private StringRedisTemplate redisTemplate;

    @Resource
    private UserRepository userRepository;


    public Long generateNextId() {
        try {
            return lockManager.executeWithLock(
                    lockKeyManager.getIdGeneratorLockKey(),
                    () -> {
                        // 1. Try Redis first
                        String redisValue = redisTemplate.opsForValue().get(ID_COUNTER_KEY);
                        if (redisValue != null) {
                            long nextId = Long.parseLong(redisValue) + ID_INCREMENT;
                            redisTemplate.opsForValue().set(ID_COUNTER_KEY, String.valueOf(nextId));
                            return nextId;
                        }

                        // 2. If Redis empty, query database for max ID
                        Long maxDbId = userRepository.findMaxUserId();
                        long nextId = (maxDbId != null) ? maxDbId + ID_INCREMENT : BASE_ID;

                        // 3. Update Redis and return
                        redisTemplate.opsForValue().set(ID_COUNTER_KEY, String.valueOf(nextId));
                        return nextId;
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ID", e);
        }
    }
}
