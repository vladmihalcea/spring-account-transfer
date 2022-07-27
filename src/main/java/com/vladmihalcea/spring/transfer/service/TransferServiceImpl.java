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
        boolean status = true;

        Account fromAccount = accountRepository.findById(fromIban).get();
        Account toAccount = accountRepository.findById(toIban).get();

        long fromBalance = fromAccount.getBalance();

        if(fromBalance >= cents) {

            fromAccount.setBalance(fromAccount.getBalance() - cents);
            toAccount.setBalance(toAccount.getBalance() + cents);
        }

        return fromAccount.getBalance();
    }
}
