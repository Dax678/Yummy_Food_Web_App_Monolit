package org.yummyfood.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 10_000) String description,
        @Size(max = 32) String phone,
        @Email @Size(max = 255) String email,
        boolean isActive
) {
}
