package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.payload.AccountCreateRequest;
import com.example.assetrecordingapp.payload.AccountCreateResult;
import com.example.assetrecordingapp.payload.AccountUpdateRequest;
import com.example.assetrecordingapp.service.AccountService;
import com.example.assetrecordingapp.vo.AccountVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping
    @RequireLogin
    public HttpResponse<AccountCreateResult> createAccount(@RequestBody AccountCreateRequest request) {
        AccountCreateResult result = accountService.createAccount(request);
        return HttpResponse.success(result);
    }

    @RequireLogin
    @PutMapping("/{id}")
    public HttpResponse<Void> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountUpdateRequest request) {
        accountService.updateAccount(id, request);
        return HttpResponse.success();
    }

    @RequireLogin
    @DeleteMapping("/{id}")
    public HttpResponse<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return HttpResponse.success();
    }

    @GetMapping()
    @RequireLogin
    public HttpResponse<List<AccountVO>> getAllAccounts() {
        List<AccountVO> accounts = accountService.getAllAccounts();
        return HttpResponse.success(accounts);
    }
}
