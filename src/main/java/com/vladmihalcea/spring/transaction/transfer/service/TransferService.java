package com.vladmihalcea.spring.transaction.transfer.service;

import org.springframework.stereotype.Service;

/**
 * @author Vlad Mihalcea
 */
@Service
public interface TransferService {

    boolean transfer(String fromIban, String toIban, long cents);
}
