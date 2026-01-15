package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.Review;
import org.yummyfood.backend.dto.response.ReviewResponse;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "restaurantId", source = "restaurant.id"),
            @Mapping(target = "menuItemId", source = "menuItem.id"),

    })
    ReviewResponse toResponse(Review review);
}
