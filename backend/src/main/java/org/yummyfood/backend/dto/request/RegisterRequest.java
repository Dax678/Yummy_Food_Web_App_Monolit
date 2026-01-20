package org.yummyfood.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.yummyfood.backend.domain.model.UserRole;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 7, max = 72) String password,
        @Size(max = 120) String fullName,
        @Size(max = 32) String phone,
        @NotNull UserRole role
) {}
