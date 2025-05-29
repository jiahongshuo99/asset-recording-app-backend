package com.example.assetrecordingapp.service.manager;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
public class DistributedLockManager {

    @Resource
    private StringRedisTemplate redisTemplate;

    public boolean tryLock(String lockKey, String lockValue, long expireTime, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, expireTime, timeUnit));
    }

    public void unlock(String lockKey, String lockValue) {
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                Collections.singletonList(lockKey),
                lockValue
        );
    }

    public <T> T executeWithLock(
            String lockKey,
            LockedOperation<T> operation
    ) throws Exception {
        String lockValue = Thread.currentThread().getId() + "-" + System.currentTimeMillis();

        try {
            if (!tryLock(lockKey, lockValue, 5L, TimeUnit.SECONDS)) {
                throw new RuntimeException("Failed to acquire lock");
            }
            return operation.execute();
        } finally {
            unlock(lockKey, lockValue);
        }
    }

    public interface LockedOperation<T> {
        T execute() throws Exception;
    }
}
