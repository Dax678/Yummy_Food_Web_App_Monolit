package org.yummyfood.backend.dto.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReviewResponse(
        UUID id,
        UUID userId,
        UUID restaurantId,
        UUID menuItemId,
        int rating,
        String comment,
        OffsetDateTime createdAt
) { }
