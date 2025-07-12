package com.damian.paynext.group.expenses.http;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record GroupExpenseCreateRequest(
        @Positive
        BigDecimal amount,

        @NotNull(message = "Description must not be null")
        String description
) {
}
