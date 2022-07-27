package com.vladmihalcea.spring.transfer;

import com.vladmihalcea.spring.transfer.config.BaseTransferTransactionManagerConfiguration;
import com.vladmihalcea.spring.transfer.domain.Account;
import com.vladmihalcea.spring.transfer.domain.AccountHolder;
import com.vladmihalcea.spring.transfer.model.Country;
import com.vladmihalcea.spring.transfer.repository.AccountRepository;
import com.vladmihalcea.spring.transfer.service.TransferService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Vlad Mihalcea
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BaseTransferTransactionManagerConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BaseTransferTransactionManagerTest {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void init() {
        try {
            transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
                AccountHolder alice = new AccountHolder()
                    .setId(1L)
                    .setFirstName("Alice")
                    .setLastName("Smith")
                    .setCountry(Country.ROMANIA);

                AccountHolder bob = new AccountHolder()
                    .setId(2L)
                    .setFirstName("Bob")
                    .setLastName("Johnson")
                    .setCountry(Country.US);

                entityManager.persist(alice);
                entityManager.persist(bob);

                entityManager.persist(
                    new Account()
                        .setIban("Alice-123")
                        .setHolder(alice)
                        .setBalance(10)
                );

                entityManager.persist(
                    new Account()
                        .setIban("Bob-456")
                        .setHolder(bob)
                        .setBalance(0)
                );
                
                return null;
            });
        } catch (TransactionException e) {
            LOGGER.error("Failure", e);
        }

    }

    @Test
    @Ignore
    public void testSerialExecution() {
        assertEquals(10L, accountRepository.getBalance("Alice-123"));
        assertEquals(0L, accountRepository.getBalance("Bob-456"));

        transferService.transfer("Alice-123", "Bob-456", 5L);

        assertEquals(5L, accountRepository.getBalance("Alice-123"));
        assertEquals(5L, accountRepository.getBalance("Bob-456"));

        transferService.transfer("Alice-123", "Bob-456", 5L);

        assertEquals(0L, accountRepository.getBalance("Alice-123"));
        assertEquals(10L, accountRepository.getBalance("Bob-456"));

        transferService.transfer("Alice-123", "Bob-456", 5L);

        assertEquals(0L, accountRepository.getBalance("Alice-123"));
        assertEquals(10L, accountRepository.getBalance("Bob-456"));
    }

    //Maximum connection count is limited to 64 due to Hikari maximum pool size
    private int threadCount = 16;

    @Test
    @Ignore
    public void testParallelExecution() throws InterruptedException {
        assertEquals(10L, accountRepository.getBalance("Alice-123"));
        assertEquals(0L, accountRepository.getBalance("Bob-456"));

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();

                    transferService.transfer("Alice-123", "Bob-456", 5L);
                } catch (Exception e) {
                    LOGGER.error("Transfer failed", e);
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        LOGGER.info("Starting threads");
        startLatch.countDown();
        endLatch.await();

        LOGGER.info("Alice's balance {}", accountRepository.getBalance("Alice-123"));
        LOGGER.info("Bob's balance {}", accountRepository.getBalance("Bob-456"));
    }

    @Test
    @Ignore
    public void testParallelExecutionUsingExecutorService() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        assertEquals(10L, accountRepository.getBalance("Alice-123"));
        assertEquals(0L, accountRepository.getBalance("Bob-456"));

        LOGGER.info("Starting threads");
        List<Future<Long>> futures = executorService.invokeAll(
            IntStream
                .range(0, threadCount)
                .mapToObj(
                    i -> (Callable<Long>) () ->
                        transferService.transfer("Alice-123", "Bob-456", 5L))
                .collect(Collectors.toList())
        );
        for (Future<Long> future : futures) {
            try {
                future.get();
            } catch (InterruptedException| ExecutionException e) {
                fail(e.getMessage());
            }
        }

        LOGGER.info("Alice's balance {}", accountRepository.getBalance("Alice-123"));
        LOGGER.info("Bob's balance {}", accountRepository.getBalance("Bob-456"));
    }
}
