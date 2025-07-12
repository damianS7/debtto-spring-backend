package com.damian.paynext.group.expenses.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class GroupExpenseNotFoundException extends ApplicationException {
    public GroupExpenseNotFoundException(String message) {
        super(message);
    }
}
