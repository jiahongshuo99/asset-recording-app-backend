package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.model.User;
import com.example.assetrecordingapp.payload.RegisterRequest;
import com.example.assetrecordingapp.repository.UserRepository;
import com.example.assetrecordingapp.service.IdGeneratorService;
import com.example.assetrecordingapp.service.manager.DistributedLockManager;
import com.example.assetrecordingapp.service.manager.LockKeyManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Optional;

public class UserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Resource
    private IdGeneratorService idGeneratorService;

    @Resource
    private DistributedLockManager lockManager;

    @Resource
    private LockKeyManager lockKeyManager;

    /**
     * @param registerRequest
     * @return
     */

    public User createUser(RegisterRequest registerRequest) {
        String lockKey = lockKeyManager.getUserRegisterLockKey(registerRequest.getPhone());
        try {
            return lockManager.executeWithLock(lockKey, () -> {
                userRepository.findByPhone(registerRequest.getPhone()).ifPresent(user -> {
                    throw new RuntimeException("用户已存在");
                });

                String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

                User user = new User();
                user.setUserId(idGeneratorService.generateNextId());
                user.setUsername(registerRequest.getUsername());
                user.setPhone(registerRequest.getPhone());
                user.setPassword(encodedPassword);

                user = userRepository.save(user);
                return user;
            });
        } catch (Exception e) {
            throw new RuntimeException("注册失败", e);
        }
    }

    public User authenticate(String phone, String rawPassword) {
        Optional<User> userOpt = userRepository.findByPhone(phone);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }
}
