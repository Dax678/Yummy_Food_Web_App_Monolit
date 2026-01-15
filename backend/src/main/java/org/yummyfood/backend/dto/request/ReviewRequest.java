package org.yummyfood.backend.dto.request;

import java.util.UUID;

public record ReviewRequest(
        UUID restaurantId,
        UUID menuItemId,
        int rating,
        String comment
) { }
