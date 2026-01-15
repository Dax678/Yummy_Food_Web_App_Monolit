package org.yummyfood.backend.dto.response;

import org.yummyfood.backend.domain.model.PaymentMethod;
import org.yummyfood.backend.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        PaymentMethod method,
        PaymentStatus status,
        BigDecimal amount,
        String provider,
        String providerRef,
        OffsetDateTime createdAt
) { }
