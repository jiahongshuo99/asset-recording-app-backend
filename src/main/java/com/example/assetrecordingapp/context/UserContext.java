package com.example.assetrecordingapp.context;

public class UserContext {
    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        userIdThreadLocal.set(userId);
    }

    public static Long getCurrentUserId() {
        return userIdThreadLocal.get();
    }

    public static void clear() {
        userIdThreadLocal.remove();
    }
}
