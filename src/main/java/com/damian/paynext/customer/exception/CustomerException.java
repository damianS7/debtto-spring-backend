package com.damian.paynext.customer.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class CustomerException extends ApplicationException {
    public CustomerException(String message) {
        super(message);
    }
}
