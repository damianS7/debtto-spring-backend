package com.damian.paynext.group.group.exception;

import com.damian.paynext.auth.exception.AuthorizationException;

public class GroupAuthorizationException extends AuthorizationException {
    public GroupAuthorizationException(String message) {
        super(message);
    }
}
