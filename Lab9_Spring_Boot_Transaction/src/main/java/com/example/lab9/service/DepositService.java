package com.example.lab9.service;

import com.example.lab9.model.Account;
import com.example.lab9.model.DepositTransaction;
import com.example.lab9.repository.AccountRepository;
import com.example.lab9.repository.DepositRepository;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositService {

    private final AccountRepository accountRepository;
    private final DepositRepository depositRepository;

    public DepositService(AccountRepository accountRepository, DepositRepository depositRepository) {
        this.accountRepository = accountRepository;
        this.depositRepository = depositRepository;
    }

    @Transactional
    public void deposit(Long accountId, Double amount) {
        if (amount == null || !Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + accountId));

        double newBalance = account.getBalance() + amount;
        if (!Double.isFinite(newBalance)) {
            throw new IllegalArgumentException("Balance is too large");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);

        DepositTransaction deposit = new DepositTransaction();
        deposit.setAmount(amount);
        deposit.setAccount(account);
        depositRepository.save(deposit);

    }
}
