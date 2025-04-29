package com.damian.debtto.auth.exception;

import com.damian.debtto.common.exception.ApplicationException;

public class AuthorizationException extends ApplicationException {
    public AuthorizationException(String message) {
        super("Unauthorized access: " + message);
    }
}
