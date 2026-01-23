package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantDetailsResponse(
        UUID id,
        String name,
        String description,
        String phone,
        String email,
        BigDecimal avgRating,
        boolean isActive,
        List<MenuItemResponse> items
) { }
