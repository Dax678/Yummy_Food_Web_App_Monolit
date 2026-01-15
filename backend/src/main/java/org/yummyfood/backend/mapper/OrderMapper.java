package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.Order;
import org.yummyfood.backend.dto.response.OrderResponse;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "restaurantId", source = "restaurant.id"),
            @Mapping(target = "placedAt", source = "createdAt")
    })
    OrderResponse toResponse(Order order);
}
