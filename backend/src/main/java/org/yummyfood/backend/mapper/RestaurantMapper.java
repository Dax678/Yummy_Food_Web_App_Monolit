package org.yummyfood.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.RestaurantRequest;
import org.yummyfood.backend.dto.response.RestaurantDetailsResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantDetailsResponse entityToApi(Restaurant entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "avgRating", ignore = true)
    })
    Restaurant apiToEntity(RestaurantRequest api);

    List<RestaurantDetailsResponse> entityListToApiList(List<Restaurant> entityList);

    List<RestaurantRequest>  apiListToEntityList(List<RestaurantRequest> apiList);
}
