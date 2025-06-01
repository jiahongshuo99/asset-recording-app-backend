package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.constant.Constants;
import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.context.UserContext;
import com.example.assetrecordingapp.exception.BizException;
import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.model.AssetSnapshot;
import com.example.assetrecordingapp.payload.*;
import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.repository.DailyAssetSummaryRepository;
import com.example.assetrecordingapp.service.AccountService;
import com.example.assetrecordingapp.service.helper.AccountHelper;
import com.example.assetrecordingapp.service.manager.DistributedLockManager;
import com.example.assetrecordingapp.service.manager.LockKeyManager;
import com.example.assetrecordingapp.vo.AccountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;
    @Resource
    private AssetSnapshotRepository assetSnapshotRepository;
    @Resource
    private DistributedLockManager distributedLockManager;
    @Resource
    private LockKeyManager lockKeyManager;
    @Resource
    private AccountHelper accountHelper;
    @Resource
    private DailyAssetSummaryRepository dailyAssetSummaryRepository;

    @Override
    @Transactional
    public AccountCreateResult createAccount(AccountCreateRequest request) {
        Long currentUserId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

        String lockKey = lockKeyManager.getAccountCreateLockKey(currentUserId);
        return distributedLockManager.executeInLock(lockKey, () -> {
            // 检查账户数量限制(只统计未删除的账户)
            long accountCount = accountRepository.countActiveByUserId(currentUserId);
            if (accountCount >= Constants.MAX_ACCOUNT_NUM) {
                throw new BizException(ErrorCodeEnum.ACCOUNT_NUM_REACH_LIMIT);
            }

            BigDecimal amountValue;
            try {
                amountValue = new BigDecimal(request.getAmount());
            } catch (Exception e) {
                throw new BizException(ErrorCodeEnum.PARAM_ERROR, "金额格式不正确");
            }

            // Create and save account
            Account account = new Account();
            account.setUserId(currentUserId);
            account.setName(request.getName());
            account.setType(request.getType());
            account.setRemark(request.getRemark());
            account.setCurrentAmount(amountValue);
            account.setIsDeleted(0);

            Account savedAccount = accountRepository.save(account);

            // Create initial asset snapshot
            AssetSnapshot snapshot = new AssetSnapshot();
            snapshot.setAccountId(savedAccount.getId());
            snapshot.setAmount(amountValue);
            assetSnapshotRepository.save(snapshot);

            List<Account> accountList = accountRepository.findActiveAccountsByUserId(currentUserId);
            List<Account> updatedAccountList = accountHelper.replaceOrAddAndGet(accountList, savedAccount);
            BigDecimal newSummaryAmount = accountHelper.sumAmounts(updatedAccountList);
            dailyAssetSummaryRepository.upsertDailySummary(currentUserId, LocalDate.now(), newSummaryAmount);

            AccountCreateResult result = new AccountCreateResult();
            result.setId(savedAccount.getId());
            return result;
        });
    }

    @Override
    @Transactional
    public void updateAccountInfo(Long id, AccountUpdateRequest request) {
        String accountLockKey = lockKeyManager.getAccountLockKey(id);
        distributedLockManager.executeInLock(accountLockKey, () -> {

            Long currentUserId = UserContext.getCurrentUserId();
            ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

            // Change to use findByIdAndNotDeleted
            Account existingAccount = accountRepository.findByIdAndNotDeleted(id)
                    .orElseThrow(() -> new BizException(ErrorCodeEnum.PARAM_ERROR));

            ErrorCodeEnum.NO_AUTH.equals(currentUserId, existingAccount, "更新账户信息鉴权失败");


            // Update account fields
            existingAccount.setName(request.getName());
            existingAccount.setType(request.getType());
            existingAccount.setRemark(request.getRemark());

            accountRepository.save(existingAccount);
            return null;
        });


    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {

        String accountLockKey = lockKeyManager.getAccountLockKey(id);
        distributedLockManager.executeInLock(accountLockKey, () -> {
            Long currentUserId = UserContext.getCurrentUserId();
            ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

            // Change to use findByIdAndNotDeleted
            Account existingAccount = accountRepository.findByIdAndNotDeleted(id)
                    .orElseThrow(() -> new BizException(ErrorCodeEnum.PARAM_ERROR));

            ErrorCodeEnum.NO_AUTH.equals(currentUserId, existingAccount.getUserId(), "删除账户鉴权失败");

            existingAccount.setIsDeleted(0);
            accountRepository.save(existingAccount);


            List<Account> accountList = accountRepository.findActiveAccountsByUserId(currentUserId);
            List<Account> updatedAccountList = accountHelper.removeAccount(accountList, id);
            BigDecimal newSummaryAmount = accountHelper.sumAmounts(updatedAccountList);
            dailyAssetSummaryRepository.upsertDailySummary(currentUserId, LocalDate.now(), newSummaryAmount);

            return null;
        });
    }

    @Override
    public List<AccountVO> getAllAccounts() {
        Long currentUserId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(currentUserId, "用户信息缺失");

        // Use the explicit query method
        List<Account> accounts = accountRepository.findActiveAccountsByUserId(currentUserId);

        // Map to AccountVO with both currentAmount and snapshot data
        return accounts.stream()
                .map(account -> {
                    AccountVO vo = new AccountVO();
                    vo.setId(account.getId());
                    vo.setName(account.getName());
                    vo.setType(account.getType());
                    vo.setRemark(account.getRemark());
                    vo.setCreatedAt(account.getCreatedAt());
                    vo.setUpdatedAt(account.getUpdatedAt());
                    vo.setCurrentAmount(account.getCurrentAmount());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AccountAmountUpdateResult updateAccountAmount(AccountAmountUpdateRequest request) {
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

        AssetSnapshot savedSnapshot = assetSnapshotRepository.save(snapshot);

        List<Account> accountList = accountRepository.findActiveAccountsByUserId(currentUserId);
        List<Account> updatedAccountList = accountHelper.replaceOrAddAndGet(accountList, account);
        BigDecimal newSummaryAmount = accountHelper.sumAmounts(updatedAccountList);
        dailyAssetSummaryRepository.upsertDailySummary(currentUserId, LocalDate.now(), newSummaryAmount);

        AccountAmountUpdateResult result = new AccountAmountUpdateResult();
        result.setId(savedSnapshot.getId());
        return result;
    }
}
