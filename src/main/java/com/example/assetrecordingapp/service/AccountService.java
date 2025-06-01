package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.payload.*;
import com.example.assetrecordingapp.vo.AccountVO;

import java.util.List;

public interface AccountService {
    AccountCreateResult createAccount(AccountCreateRequest request);

    void updateAccountInfo(Long id, AccountUpdateRequest request);

    void deleteAccount(Long id);

    List<AccountVO> getAllAccounts();

    AccountAmountUpdateResult updateAccountAmount(AccountAmountUpdateRequest request);
}
