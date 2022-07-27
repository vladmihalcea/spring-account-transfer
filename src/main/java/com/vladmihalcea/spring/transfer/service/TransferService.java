package com.vladmihalcea.spring.transfer.service;

import org.springframework.stereotype.Service;

/**
 * @author Vlad Mihalcea
 */
@Service
public interface TransferService {

    long transfer(String fromIban, String toIban, long cents);
}
