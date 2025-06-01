package com.example.assetrecordingapp.controller;

import com.example.assetrecordingapp.annotation.RequireLogin;
import com.example.assetrecordingapp.dto.HttpResponse;
import com.example.assetrecordingapp.payload.*;
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

    @PostMapping("/create")
    @RequireLogin
    public HttpResponse<AccountCreateResult> createAccount(@RequestBody AccountCreateRequest request) {
        AccountCreateResult result = accountService.createAccount(request);
        return HttpResponse.success(result);
    }

    @RequireLogin
    @PostMapping("/update-info/{id}")
    public HttpResponse<Void> updateAccountInfo(
            @PathVariable Long id,
            @RequestBody AccountUpdateRequest request) {
        accountService.updateAccountInfo(id, request);
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

    @RequireLogin
    @PostMapping("/update-amount")
    public HttpResponse<AccountAmountUpdateResult> updateAccountAmount(@RequestBody AccountAmountUpdateRequest request) {
        AccountAmountUpdateResult result = accountService.updateAccountAmount(request);
        return HttpResponse.success(result);
    }
}
