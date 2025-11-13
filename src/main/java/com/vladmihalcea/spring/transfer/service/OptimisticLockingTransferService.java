package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.domain.Account;
import com.vladmihalcea.spring.transfer.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vlad Mihalcea
 */
@Service
public class OptimisticLockingTransferService {

    private final AccountRepository accountRepository;

    public OptimisticLockingTransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public long transfer(String fromIban, String toIban, long cents) {
        Account fromAccount = accountRepository.findByIbanWithHolder(fromIban);

        long fromBalance = fromAccount.getBalance();

        if (fromBalance >= cents) {
            fromAccount.addToBalance(Math.negateExact(cents));
            accountRepository.findByIbanWithHolder(toIban).addToBalance(cents);
        }

        return fromAccount.getBalance();
    }
}