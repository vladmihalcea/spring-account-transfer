package com.vladmihalcea.spring.transfer.domain;

import javax.persistence.*;

/**
 * @author Vlad Mihalcea
 */
@Entity(name = "Account")
@Table(name = "account")
public class Account {

    @Id
    private String iban;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountHolder holder;

    private long balance;

    public String getIban() {
        return iban;
    }

    public Account setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public AccountHolder getHolder() {
        return holder;
    }

    public Account setHolder(AccountHolder holder) {
        this.holder = holder;
        return this;
    }

    public long getBalance() {
        return balance;
    }

    public Account setBalance(long balance) {
        this.balance = balance;
        return this;
    }
}
