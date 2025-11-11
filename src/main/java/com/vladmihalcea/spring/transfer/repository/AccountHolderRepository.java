package com.vladmihalcea.spring.transfer.repository;

import com.vladmihalcea.spring.transfer.domain.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Vlad Mihalcea
 */
@Repository
@Transactional(readOnly = true)
public interface AccountHolderRepository extends JpaRepository<AccountHolder, String> {

}
