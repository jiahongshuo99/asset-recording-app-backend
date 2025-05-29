package com.example.assetrecordingapp.service;


import com.example.assetrecordingapp.model.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);

    Account updateAccount(Long id, Account account);

    void deleteAccount(Long id);

    List<Account> getAllAccounts(Long userId);
}
