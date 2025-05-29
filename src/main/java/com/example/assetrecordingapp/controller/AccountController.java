package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.model.Account;
import com.example.assetrecordingapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService accountService;
    
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping
    public HttpResponse<Account> createAccount(@RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account);
        return HttpResponse.success(createdAccount);
    }
    
    @PutMapping("/{id}")
    public HttpResponse<Account> updateAccount(
            @PathVariable Long id, 
            @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return HttpResponse.success(updatedAccount);
    }
    
    @DeleteMapping("/{id}")
    public HttpResponse<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return HttpResponse.success();
    }
    
    @GetMapping("/{userId}")
    public HttpResponse<List<Account>> getAllAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAllAccounts(userId);
        return HttpResponse.success(accounts);
    }
}
