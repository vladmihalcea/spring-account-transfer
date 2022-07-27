package com.vladmihalcea.spring.transfer.repository;

import com.vladmihalcea.spring.transfer.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vlad Mihalcea
 */
@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, String> {

    @Query("select balance from Account where iban = :iban")
    long getBalance(@Param("iban") String iban);
}
