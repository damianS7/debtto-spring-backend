package com.damian.paynext.group.expenses.exception;

import com.damian.paynext.auth.exception.AuthorizationException;

public class GroupExpenseAuthorizationException extends AuthorizationException {
    public GroupExpenseAuthorizationException(String message) {
        super(message);
    }
}
