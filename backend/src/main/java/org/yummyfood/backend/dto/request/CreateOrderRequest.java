package org.yummyfood.backend.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        UUID restaurantId,
        List<OrderItemRequest> items,
        String notes
) {
    public record OrderItemRequest(
            UUID menuItemId,
            int quantity
    ) { }
}
