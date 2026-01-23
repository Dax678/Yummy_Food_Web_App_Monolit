package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record MenuItemResponse(
        UUID id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl
) {
}
