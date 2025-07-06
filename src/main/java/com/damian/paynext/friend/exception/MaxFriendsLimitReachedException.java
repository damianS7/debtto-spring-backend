package com.damian.paynext.friend.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class MaxFriendsLimitReachedException extends ApplicationException {
    public MaxFriendsLimitReachedException(String message) {
        super(message);
    }
}
