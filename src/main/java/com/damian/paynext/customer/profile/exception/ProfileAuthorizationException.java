package com.damian.paynext.customer.profile.exception;

import com.damian.paynext.auth.exception.AuthorizationException;

public class ProfileAuthorizationException extends AuthorizationException {
    public ProfileAuthorizationException(String message) {
        super(message);
    }
}
