package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.domain.Account;
import com.vladmihalcea.spring.transfer.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vlad Mihalcea
 */
@Service
public class TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public long transfer(String fromIban, String toIban, long cents) {
        Account fromAccount = accountRepository.findByIbanWithHolder(fromIban);

        long fromBalance = fromAccount.getBalance();

        if (fromBalance >= cents) {
            accountRepository.setBalance(fromIban, Math.negateExact(cents));
            accountRepository.setBalance(toIban, cents);
        }

        return accountRepository.getBalance(fromIban);
    }
}