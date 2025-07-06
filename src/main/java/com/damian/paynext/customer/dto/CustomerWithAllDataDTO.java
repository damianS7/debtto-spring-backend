package com.damian.paynext.customer.dto;

import com.damian.paynext.customer.CustomerRole;
import com.damian.paynext.customer.profile.ProfileDTO;

import java.time.Instant;

public record CustomerWithAllDataDTO(
        Long id,
        String email,
        CustomerRole role,
        ProfileDTO profile,
        Instant createdAt,
        Instant updatedAt
) {
}