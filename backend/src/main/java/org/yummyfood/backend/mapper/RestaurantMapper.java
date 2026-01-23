package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.RestaurantRequest;
import org.yummyfood.backend.dto.response.RestaurantDetailsResponse;
import org.yummyfood.backend.dto.response.RestaurantResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "items", source = "menuItems")
    RestaurantDetailsResponse entityToApi(Restaurant entity);

    @Mapping(target = "items", source = "menuItems")
    RestaurantResponse entityToResponse(Restaurant entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "avgRating", ignore = true),
            @Mapping(target = "owner", ignore = true)
    })
    Restaurant apiToEntity(RestaurantRequest api);

    @Mapping(target = "items", source = "menuItems")
    List<RestaurantResponse> entityListToApiList(List<Restaurant> entityList);

    List<RestaurantRequest>  apiListToEntityList(List<RestaurantRequest> apiList);
}
