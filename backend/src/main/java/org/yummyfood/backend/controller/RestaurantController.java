package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.RestaurantRequest;
import org.yummyfood.backend.dto.response.RestaurantDetailsResponse;
import org.yummyfood.backend.dto.response.RestaurantResponse;
import org.yummyfood.backend.mapper.MenuItemMapper;
import org.yummyfood.backend.mapper.RestaurantMapper;
import org.yummyfood.backend.service.MenuItemService;
import org.yummyfood.backend.service.RestaurantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;

    @PostMapping
    public ResponseEntity<RestaurantDetailsResponse> create(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant saved = restaurantService.createRestaurant(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantMapper.entityToApi(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailsResponse> update(@PathVariable UUID id, @Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant updated = restaurantService.updateRestaurant(id, entity);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityToApi(updated));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDetailsResponse>> listAllActive(
            @RequestParam(required = false, defaultValue = "true") boolean isActive,
            @RequestParam(required = false) String search
    ) {
        List<Restaurant> restaurantList;
        if (search == null || search.isBlank()) {
            restaurantList = restaurantService.listActive(isActive);
        } else {
            restaurantList = restaurantService.searchByNameAndActive(search, isActive);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityListToApiList(restaurantList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable UUID id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RestaurantResponse(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getDescription(),
                        restaurant.getPhone(),
                        restaurant.getEmail(),
                        menuItemService.listByRestaurantId(id).stream()
                                .map(menuItemMapper::toRestaurantMenuItemResponse)
                                .toList()
                ));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantDetailsById(@PathVariable UUID id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityToApi(restaurantService.getRestaurantById(id)));
    }

}
