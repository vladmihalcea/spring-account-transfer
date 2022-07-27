package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.domain.Account;
import com.vladmihalcea.spring.transfer.model.Country;
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
        Account fromAccount = accountRepository.findByIbanWithHolder(fromIban);

        Country requestCountry = geoLocationService.resolveCountry(UserRequestContext.getIpAddress());
        if(requestCountry != null && requestCountry != fromAccount.getHolder().getCountry()) {
            throw new IllegalArgumentException(
                String.format(
                    "The account holder is from [%s], but the request comes from [%s]",
                    fromAccount.getHolder().getCountry(),
                    requestCountry
                )
            );
        }

        long fromBalance = fromAccount.getBalance();

        if (fromBalance >= cents) {
            accountRepository.setBalance(fromIban, Math.negateExact(cents));
            accountRepository.setBalance(toIban, cents);
        }

        long availableFromBalance = accountRepository.getBalance(fromIban);
        return availableFromBalance;
    }
}
