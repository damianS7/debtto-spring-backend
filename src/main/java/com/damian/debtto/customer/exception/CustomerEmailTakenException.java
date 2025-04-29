package com.damian.debtto.customer.exception;

public class CustomerEmailTakenException extends CustomerException {
    public CustomerEmailTakenException(String email) {
        super("Email " + email + " is already taken.");
    }
}
