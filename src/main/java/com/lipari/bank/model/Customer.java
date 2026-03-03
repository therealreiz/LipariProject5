package com.lipari.bank.model;

public class Customer {

    private long   id;
    private String fiscalCode;
    private String firstName;
    private String lastName;

    public Customer(String fiscalCode, String firstName, String lastName) {
        this.fiscalCode = fiscalCode;
        this.firstName  = firstName;
        this.lastName   = lastName;
    }

    public Customer(long id, String fiscalCode, String firstName, String lastName) {
        this.id         = id;
        this.fiscalCode = fiscalCode;
        this.firstName  = firstName;
        this.lastName   = lastName;
    }

    public long   getId()         { return id; }
    public void   setId(long id)  { this.id = id; }
    public String getFiscalCode() { return fiscalCode; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getFullName()   { return firstName + " " + lastName; }

    @Override
    public String toString() {
        return String.format("Customer{id=%d, cf='%s', nome='%s'}", id, fiscalCode, getFullName());
    }
}
