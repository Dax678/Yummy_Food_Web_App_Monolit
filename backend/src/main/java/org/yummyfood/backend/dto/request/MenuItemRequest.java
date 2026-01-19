package org.yummyfood.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 10_000) String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        boolean isAvailable,
        @Size(max = 500) String imageUrl
) { }
