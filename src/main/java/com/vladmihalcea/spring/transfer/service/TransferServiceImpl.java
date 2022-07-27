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
public class TransferServiceImpl implements TransferService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public long transfer(String fromIban, String toIban, long cents) {
        long fromBalance = accountRepository.getBalance(fromIban);

        if(fromBalance >= cents) {
            accountRepository.setBalance(fromIban, Math.negateExact(cents));
            accountRepository.setBalance(toIban, cents);
        }

        long availableFromBalance = accountRepository.getBalance(fromIban);
        return availableFromBalance;
    }
}
