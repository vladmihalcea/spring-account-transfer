package com.vladmihalcea.spring.transfer.domain;

import com.vladmihalcea.spring.transfer.model.Country;

import javax.persistence.*;

/**
 * @author Vlad Mihalcea
 */
@Entity(name = "AccountHolder")
@Table(name = "account_holder")
public class AccountHolder {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Country country;

    public Long getId() {
        return id;
    }

    public AccountHolder setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AccountHolder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountHolder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Country getCountry() {
        return country;
    }

    public AccountHolder setCountry(Country country) {
        this.country = country;
        return this;
    }
}
