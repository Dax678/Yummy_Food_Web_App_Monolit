package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.dto.response.MenuItemDetailsResponse;
import org.yummyfood.backend.dto.response.MenuItemResponse;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mappings({
            @Mapping(target = "restaurantId", source = "restaurant.id")
    })
    MenuItemDetailsResponse toDetailsResponse(MenuItem menuItem);

    MenuItemResponse toRestaurantMenuItemResponse(MenuItem menuItem);
}
