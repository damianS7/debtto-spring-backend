package com.damian.paynext.group.group.dto;

public record GroupCustomerOwnerDTO(
        Long customerId,
        String customerName,
        String avatarFilename
) {
}
