package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantResponse(
        UUID id,
        String name,
        String description,
        String phone,
        String email,
        List<MenuItemResponse> items
) {
    public record MenuItemResponse(
            UUID id,
            String name,
            String description,
            BigDecimal price,
            String imageUrl
    ) { }
}
