package com.damian.debtto.customer.exception;

import com.damian.debtto.common.exception.ApplicationException;

public class CustomerException extends ApplicationException {
    public CustomerException(String message) {
        super(message);
    }
}
