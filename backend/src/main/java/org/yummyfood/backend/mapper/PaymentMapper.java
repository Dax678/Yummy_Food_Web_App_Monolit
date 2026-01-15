package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.yummyfood.backend.domain.Payment;
import org.yummyfood.backend.dto.response.PaymentResponse;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
