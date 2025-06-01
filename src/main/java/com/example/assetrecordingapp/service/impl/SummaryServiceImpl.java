package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.constant.ErrorCodeEnum;
import com.example.assetrecordingapp.context.UserContext;
import com.example.assetrecordingapp.model.DailyAssetSummary;
import com.example.assetrecordingapp.pojo.AssetTrendData;
import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.repository.AssetSnapshotRepository;
import com.example.assetrecordingapp.repository.DailyAssetSummaryRepository;
import com.example.assetrecordingapp.service.SummaryService;
import com.example.assetrecordingapp.service.helper.AssetSummaryHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class SummaryServiceImpl implements SummaryService {

    private final AccountRepository accountRepository;
    private final AssetSnapshotRepository assetSnapshotRepository;
    @Resource
    private DailyAssetSummaryRepository dailyAssetSummaryRepository;
    @Resource
    private AssetSummaryHelper assetSummaryHelper;

    @Autowired
    public SummaryServiceImpl(AccountRepository accountRepository,
                              AssetSnapshotRepository assetSnapshotRepository) {
        this.accountRepository = accountRepository;
        this.assetSnapshotRepository = assetSnapshotRepository;
    }

    @Override
    public BigDecimal getTotalAssets() {
        Long userId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(userId, "用户信息缺失");

        return accountRepository.sumCurrentAmountByUserId(userId);
    }

    @Override
    public AssetTrendData getAssetTrend(LocalDate startDate, LocalDate endDate) {
        Long userId = UserContext.getCurrentUserId();
        ErrorCodeEnum.SYSTEM_ERROR.isNotNull(userId, "用户信息缺失");

        List<DailyAssetSummary> dailyAssetSummaryList = dailyAssetSummaryRepository.findByUserIdAndDateRange(userId, startDate, endDate);

        return assetSummaryHelper.buildAssetTrend(startDate, endDate, dailyAssetSummaryList);
    }


}
