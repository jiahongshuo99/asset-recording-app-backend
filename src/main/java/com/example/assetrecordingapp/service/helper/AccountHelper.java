package com.example.assetrecordingapp.service.helper;

import com.example.assetrecordingapp.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountHelper {
    public List<Account> replaceOrAddAndGet(List<Account> accounts, Account newAccount) {
        accounts = Optional.ofNullable(accounts).orElse(new ArrayList<>());
        if (Objects.isNull(newAccount)) {
            return accounts;
        }

        List<Account> newAccounts = accounts.stream().filter(
                account -> !Objects.equals(account.getId(), newAccount.getId())
        ).collect(Collectors.toList());
        newAccounts.add(newAccount);
        return newAccounts;
    }

    public List<Account> removeAccount(List<Account> accounts, Long accountId) {
        accounts = Optional.ofNullable(accounts).orElse(new ArrayList<>());
        if (Objects.isNull(accountId)) {
            return accounts;
        }
        return accounts.stream().filter(account -> !Objects.equals(account.getId(), accountId)).collect(Collectors.toList());
    }

    public BigDecimal sumAmounts(List<Account> accounts) {
        accounts = Optional.ofNullable(accounts).orElse(new ArrayList<>());
        return accounts.stream()
            .map(Account::getCurrentAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
