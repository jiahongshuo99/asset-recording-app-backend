package com.example.assetrecordingapp.service.manager;
import org.springframework.stereotype.Service;

@Service
public class LockKeyManager {
    private static final String LOCK_PREFIX = "lock:";

    public String getIdGeneratorLockKey() {
        return LOCK_PREFIX + "id_generator";
    }

    public String getUserRegisterLockKey(String phone) {
        return LOCK_PREFIX + "user_register:" + phone;
    }
}
