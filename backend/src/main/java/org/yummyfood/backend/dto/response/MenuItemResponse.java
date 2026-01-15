package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        UUID restaurantId,
        String name,
        String description,
        BigDecimal price,
        boolean isAvailable,
        String imageUrl,
        OffsetDateTime createdAt
) { }
