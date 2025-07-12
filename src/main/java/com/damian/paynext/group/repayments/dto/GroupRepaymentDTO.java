package com.damian.paynext.group.repayments.dto;

import java.math.BigDecimal;

public record GroupRepaymentDTO(
        Long id,
        Long groupId,
        Long customerPayerId,
        BigDecimal amount
) {
}