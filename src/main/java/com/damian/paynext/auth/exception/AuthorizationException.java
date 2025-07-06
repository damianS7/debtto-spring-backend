package com.damian.paynext.auth.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class AuthorizationException extends ApplicationException {
    public AuthorizationException(String message) {
        super(message);
    }
}
