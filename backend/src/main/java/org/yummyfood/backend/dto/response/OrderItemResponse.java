package org.yummyfood.backend.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID id,
        UUID menuItemId,
        String nameSnapshot,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) { }
