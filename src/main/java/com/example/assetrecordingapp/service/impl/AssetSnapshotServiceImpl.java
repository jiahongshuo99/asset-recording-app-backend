package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.context.UserContext;
import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.exception.BizException;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateRequest;
import com.example.assetrecordingapp.payload.AssetSnapshotCreateResult;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AssetSnapshotServiceImpl implements AssetSnapshotService {

    private final AssetSnapshotRepository assetSnapshotRepository;
    private final AccountRepository accountRepository;

    public AssetSnapshotServiceImpl(AssetSnapshotRepository assetSnapshotRepository, 
                                  AccountRepository accountRepository) {
        this.assetSnapshotRepository = assetSnapshotRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AssetSnapshotCreateResult createSnapshot(AssetSnapshotCreateRequest request) {
        // 获取当前用户ID
        Long currentUserId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

        // 验证金额格式
        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getAmount());
        } catch (Exception e) {
            log.error("[createSnapshot] Invalid amount value: {}", request.getAmount());
            throw new BizException(ErrorCodeEnum.PARAM_ERROR);
        }

        // 检查账户存在性和权限
        Account account = accountRepository.findByIdAndNotDeleted(request.getAccountId())
                .orElseThrow(() -> new BizException(ErrorCodeEnum.PARAM_ERROR, "账户不存在"));
        
        ErrorCodeEnum.NO_AUTH.equals(currentUserId, account.getUserId(), "无权操作该账户");


        // 更新account表的current_amount
        account.setCurrentAmount(amount);
        accountRepository.save(account);

        // 创建新的快照记录
        AssetSnapshot snapshot = new AssetSnapshot();
        snapshot.setAccountId(request.getAccountId());
        snapshot.setAmount(amount);
        snapshot.setCreatedAt(LocalDateTime.now());
        
        AssetSnapshot savedSnapshot = assetSnapshotRepository.save(snapshot);

        AssetSnapshotCreateResult result = new AssetSnapshotCreateResult();
        result.setId(savedSnapshot.getId());
        return result;
    }

    @Override
    public List<AssetSnapshot> getSnapshotsByAccountId(Long accountId) {
        // 获取当前用户ID
        Long currentUserId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

        // 检查账户存在性和权限
        Account account = accountRepository.findByIdAndNotDeleted(accountId)
                .orElseThrow(() -> new BizException(ErrorCodeEnum.PARAM_ERROR, "账户不存在"));
        
        ErrorCodeEnum.NO_AUTH.equals(currentUserId, account.getUserId(), "无权查看该账户快照");

        return assetSnapshotRepository.findByAccountIdOrderBySnapshotTimeDesc(accountId);
    }
}
