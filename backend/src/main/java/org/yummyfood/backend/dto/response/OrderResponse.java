package org.yummyfood.backend.dto.response;

import org.yummyfood.backend.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID userId,
        UUID restaurantId,
        OrderStatus status,
        BigDecimal itemsTotal,
        BigDecimal deliveryFee,
        BigDecimal grandTotal,
        String notes,
        OffsetDateTime placedAt,
        List<OrderItemResponse> items,
        PaymentResponse payment
) { }
