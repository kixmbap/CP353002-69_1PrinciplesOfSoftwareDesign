package com.example.lab9.service;

import com.example.lab9.model.Account;
import com.example.lab9.repository.AccountRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account form) {
        if (form.getAccountNumber() == null || form.getAccountNumber().isBlank()
                || form.getOwnerName() == null || form.getOwnerName().isBlank()) {
            throw new IllegalArgumentException("Account number and owner name are required");
        }
        if (form.getBalance() == null || !Double.isFinite(form.getBalance()) || form.getBalance() < 0) {
            throw new IllegalArgumentException("Balance must be non-negative");
        }

        Account account = new Account();
        account.setAccountNumber(form.getAccountNumber());
        account.setOwnerName(form.getOwnerName());
        account.setBalance(form.getBalance());
        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + id));
    }
}
