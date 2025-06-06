package com.vladmihalcea.spring.transfer.service;

import com.vladmihalcea.spring.transfer.config.TransferServiceConfiguration;
import com.vladmihalcea.spring.transfer.domain.Account;
import com.vladmihalcea.spring.transfer.domain.AccountHolder;
import com.vladmihalcea.spring.transfer.model.Country;
import com.vladmihalcea.spring.transfer.repository.AccountRepository;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test for {@link TransferService}
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TransferServiceConfiguration.class)
@Transactional
public class AITransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    private @PersistenceContext EntityManager entityManager;

    private static final String FROM_IBAN = "FR1420041010050500013M02606";
    private static final String TO_IBAN = "RO49AAAA1B31007593840000";
    private static final long INITIAL_FROM_BALANCE = 1000L;
    private static final long INITIAL_TO_BALANCE = 0L;

    @BeforeEach
    public void setUp() {
        // Create account holders
        AccountHolder fromAccountHolder = new AccountHolder()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setCountry(Country.FRANCE);

        AccountHolder toAccountHolder = new AccountHolder()
                .setId(2L)
                .setFirstName("Jane")
                .setLastName("Smith")
                .setCountry(Country.ROMANIA);

        // Save account holders first using EntityManager
        if (entityManager != null) {
            entityManager.persist(fromAccountHolder);
            entityManager.persist(toAccountHolder);
            entityManager.flush();
        } else {
            System.out.println("EntityManager is null!");
        }

        // Create accounts
        Account fromAccount = new Account()
                .setIban(FROM_IBAN)
                .setHolder(fromAccountHolder)
                .setBalance(INITIAL_FROM_BALANCE);

        Account toAccount = new Account()
                .setIban(TO_IBAN)
                .setHolder(toAccountHolder)
                .setBalance(INITIAL_TO_BALANCE);

        // Save accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    @Test
    public void testSuccessfulTransfer() {
        // Given
        long transferAmount = 500L;

        // When
        long newFromBalance = transferService.transfer(FROM_IBAN, TO_IBAN, transferAmount);

        // Then
        assertEquals(INITIAL_FROM_BALANCE - transferAmount, newFromBalance);
        assertEquals(INITIAL_FROM_BALANCE - transferAmount, accountRepository.getBalance(FROM_IBAN));
        assertEquals(INITIAL_TO_BALANCE + transferAmount, accountRepository.getBalance(TO_IBAN));
    }

    @Test
    public void testInsufficientFundsTransfer() {
        // Given
        long transferAmount = INITIAL_FROM_BALANCE + 100L; // More than available

        // When
        long newFromBalance = transferService.transfer(FROM_IBAN, TO_IBAN, transferAmount);

        // Then
        // No transfer should occur, balances should remain unchanged
        assertEquals(INITIAL_FROM_BALANCE, newFromBalance);
        assertEquals(INITIAL_FROM_BALANCE, accountRepository.getBalance(FROM_IBAN));
        assertEquals(INITIAL_TO_BALANCE, accountRepository.getBalance(TO_IBAN));
    }

    @Test
    public void testZeroAmountTransfer() {
        // Given
        long transferAmount = 0L;

        // When
        long newFromBalance = transferService.transfer(FROM_IBAN, TO_IBAN, transferAmount);

        // Then
        // Zero amount transfers should be treated as sufficient funds but no actual transfer
        assertEquals(INITIAL_FROM_BALANCE, newFromBalance);
        assertEquals(INITIAL_FROM_BALANCE, accountRepository.getBalance(FROM_IBAN));
        assertEquals(INITIAL_TO_BALANCE, accountRepository.getBalance(TO_IBAN));
    }
}
