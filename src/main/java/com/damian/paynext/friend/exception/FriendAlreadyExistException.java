package com.damian.paynext.friend.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class FriendAlreadyExistException extends ApplicationException {
    public FriendAlreadyExistException(String message) {
        super(message);
    }
}
