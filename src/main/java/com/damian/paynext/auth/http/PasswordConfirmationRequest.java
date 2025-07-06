package com.damian.paynext.auth.http;

import jakarta.validation.constraints.NotNull;

public record PasswordConfirmationRequest(
        @NotNull(
                message = "Password must not be null"
        )
        String password
) {
}
