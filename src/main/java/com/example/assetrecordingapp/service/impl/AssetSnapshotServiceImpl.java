package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.context.UserContext;
import com.example.assetrecordingapp.exception.BizException;
import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.service.AssetSnapshotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
