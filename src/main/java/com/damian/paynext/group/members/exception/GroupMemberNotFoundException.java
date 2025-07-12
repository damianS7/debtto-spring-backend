package com.damian.paynext.group.members.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class GroupMemberNotFoundException extends ApplicationException {
    public GroupMemberNotFoundException(String message) {
        super(message);
    }
}
