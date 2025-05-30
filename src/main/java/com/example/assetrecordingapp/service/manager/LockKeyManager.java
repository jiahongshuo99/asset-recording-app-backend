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

    public String getAccountLockKey(Long accountId) {
        return LOCK_PREFIX + "account:" + accountId;
    }

    public String getAccountCreateLockKey(Long currentUserId) {
        return LOCK_PREFIX + "create_account:user_id" + currentUserId;
    }
}
