package com.example.assetrecordingapp.service;

import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.payload.AccountCreateRequest;
import com.example.assetrecordingapp.payload.AccountCreateResult;
import com.example.assetrecordingapp.payload.AccountUpdateRequest;
import com.example.assetrecordingapp.vo.AccountVO;

import java.util.List;

public interface AccountService {
    AccountCreateResult createAccount(AccountCreateRequest request);

    void updateAccount(Long id, AccountUpdateRequest request);

    void deleteAccount(Long id);

    List<AccountVO> getAllAccounts();
}
