package com.damian.paynext.friend.exception;

import com.damian.paynext.auth.exception.AuthorizationException;

public class FriendAuthorizationException extends AuthorizationException {
    public FriendAuthorizationException(String message) {
        super(message);
    }
}
