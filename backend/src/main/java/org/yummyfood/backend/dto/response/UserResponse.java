package org.yummyfood.backend.dto.response;

import org.yummyfood.backend.domain.model.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String fullName,
        String phone,
        UserRole role,
        OffsetDateTime createdAt
) { }
