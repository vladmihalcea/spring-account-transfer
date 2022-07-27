package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.repository.AccountRepository;
import com.vladmihalcea.spring.util.UserRequestContext;
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

    @Autowired
    private GeoLocationService geoLocationService;

    @Override
    @Transactional
    public long transfer(String fromIban, String toIban, long cents) {
        long fromBalance = accountRepository.getBalance(fromIban);

        //Call external service
        geoLocationService.resolveCountry(UserRequestContext.getIpAddress());

        if (fromBalance >= cents) {
            accountRepository.setBalance(fromIban, Math.negateExact(cents));
            accountRepository.setBalance(toIban, cents);
        }

        long availableFromBalance = accountRepository.getBalance(fromIban);
        return availableFromBalance;
    }
}
