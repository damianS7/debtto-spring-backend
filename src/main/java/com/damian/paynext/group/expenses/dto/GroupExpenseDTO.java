package com.damian.paynext.group.expenses.dto;

import java.math.BigDecimal;

public record GroupExpenseDTO(
        Long id,
        Long groupId,
        Long customerPayerId,
        BigDecimal amount,
        String description
) {
}