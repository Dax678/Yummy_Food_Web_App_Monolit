package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.OrderItem;
import org.yummyfood.backend.dto.response.OrderItemResponse;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mappings({
            @Mapping(target = "menuItemId", source = "menuItem.id")
    })
    OrderItemResponse toResponse(OrderItem orderItem);
}
