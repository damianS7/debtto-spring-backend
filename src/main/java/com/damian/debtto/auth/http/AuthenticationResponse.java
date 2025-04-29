package com.damian.debtto.auth.http;

import com.damian.debtto.customer.CustomerDTO;

public record AuthenticationResponse(
        CustomerDTO customer,
        String token
) {
}

