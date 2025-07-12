package com.damian.paynext.group.repayments.exception;

import com.damian.paynext.common.exception.ApplicationException;

public class GroupRepaymentNotFoundException extends ApplicationException {
    public GroupRepaymentNotFoundException(String message) {
        super(message);
    }
}
