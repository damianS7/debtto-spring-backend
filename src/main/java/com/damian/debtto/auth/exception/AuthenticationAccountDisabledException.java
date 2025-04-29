package com.damian.debtto.auth.exception;

public class AuthenticationAccountDisabledException extends AuthenticationException {
    public AuthenticationAccountDisabledException(String message) {
        super("Account is disabled.");
    }
}
