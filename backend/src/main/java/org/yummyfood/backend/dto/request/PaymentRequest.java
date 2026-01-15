package org.yummyfood.backend.dto.request;

import org.yummyfood.backend.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest(
        UUID orderId,
        PaymentMethod method,
        BigDecimal amount
) { }
