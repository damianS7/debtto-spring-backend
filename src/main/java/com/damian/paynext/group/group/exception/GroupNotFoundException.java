package com.damian.paynext.group.group.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class GroupNotFoundException extends ApplicationException {
    public GroupNotFoundException(String message) {
        super(message);
    }
}
