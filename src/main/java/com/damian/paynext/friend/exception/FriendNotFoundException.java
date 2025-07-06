package com.damian.paynext.friend.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class FriendNotFoundException extends ApplicationException {
    public FriendNotFoundException(String message) {
        super(message);
    }
}
