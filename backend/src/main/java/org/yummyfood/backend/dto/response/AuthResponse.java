package org.yummyfood.backend.dto.response;

public record AuthResponse(
        String token,
        UserResponse user
) {}
