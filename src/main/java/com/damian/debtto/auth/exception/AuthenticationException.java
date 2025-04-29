package com.damian.debtto.auth.exception;

import com.damian.debtto.common.exception.ApplicationException;

public class AuthenticationException extends ApplicationException {
    public AuthenticationException(String message) {
        super(message);
    }
}
