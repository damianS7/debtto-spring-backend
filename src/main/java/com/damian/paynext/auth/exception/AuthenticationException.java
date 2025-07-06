package com.damian.paynext.auth.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class AuthenticationException extends ApplicationException {
    public AuthenticationException(String message) {
        super(message);
    }
}
