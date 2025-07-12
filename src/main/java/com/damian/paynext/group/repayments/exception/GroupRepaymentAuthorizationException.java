package com.damian.paynext.group.repayments.exception;

import com.damian.paynext.auth.exception.AuthorizationException;

public class GroupRepaymentAuthorizationException extends AuthorizationException {
    public GroupRepaymentAuthorizationException(String message) {
        super(message);
    }
}
