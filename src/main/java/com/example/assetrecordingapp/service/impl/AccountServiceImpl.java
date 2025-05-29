package com.example.assetrecordingapp.service.impl;

import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.repository.AccountRepository;
import com.example.assetrecordingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Account createAccount(Account account) {
        // Validate account data
        if (account.getName() == null || account.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Account name cannot be empty");
        }
        
        // Set timestamps
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public Account updateAccount(Long id, Account account) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        
        // Update fields
        existingAccount.setName(account.getName());
        existingAccount.setType(account.getType());
        existingAccount.setRemark(account.getRemark());
        existingAccount.setUpdatedAt(LocalDateTime.now());
        
        return accountRepository.save(existingAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public List<Account> getAllAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }
}
