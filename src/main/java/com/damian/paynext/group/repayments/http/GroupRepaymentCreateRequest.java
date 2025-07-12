package com.damian.paynext.group.repayments.http;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record GroupRepaymentCreateRequest(
        @Positive
        BigDecimal amount
) {
}
