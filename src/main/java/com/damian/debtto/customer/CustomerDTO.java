package com.damian.debtto.customer;

import com.damian.debtto.customer.profile.ProfileDTO;

import java.time.Instant;

public record CustomerDTO(
        Long id,
        String email,
        CustomerRole role,
        CustomerAccountStatus accountStatus,
        ProfileDTO profile,
        Instant createdAt,
        Instant updatedAt
) {
}