package com.lipari.bank.model;

import java.math.BigDecimal;

public class Account {

    private long       id;
    private String     iban;
    private BigDecimal balance;
    private long       customerId;

    public Account(String iban, BigDecimal balance, long customerId) {
        this.iban       = iban;
        this.balance    = balance;
        this.customerId = customerId;
    }

    public Account(long id, String iban, BigDecimal balance, long customerId) {
        this.id         = id;
        this.iban       = iban;
        this.balance    = balance;
        this.customerId = customerId;
    }

    public long       getId()         { return id; }
    public void       setId(long id)  { this.id = id; }
    public String     getIban()       { return iban; }
    public BigDecimal getBalance()    { return balance; }
    public long       getCustomerId() { return customerId; }

    @Override
    public String toString() {
        return String.format("Account{id=%d, iban='%s', saldo=%s\u20AC}", id, iban, balance);
    }
}
