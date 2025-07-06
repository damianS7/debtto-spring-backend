package com.damian.paynext.group.http;

import jakarta.validation.constraints.NotBlank;

public record GroupUpdateRequest(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotBlank(message = "Description must not be blank")
        String description
) {
}
