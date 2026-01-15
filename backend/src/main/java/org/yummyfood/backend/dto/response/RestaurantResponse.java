package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String description,
        String phone,
        String email,
        boolean isActive,
        BigDecimal avgRating,
        OffsetDateTime createdAt
) { }
