package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.RestaurantRequest;
import org.yummyfood.backend.dto.response.RestaurantResponse;
import org.yummyfood.backend.mapper.RestaurantMapper;
import org.yummyfood.backend.service.RestaurantService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant saved = restaurantService.createRestaurant(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantMapper.entityToApi(saved));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> listAllActive(
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityToApi(restaurantService.getRestaurantById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(@PathVariable UUID id, @Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant updated = restaurantService.updateRestaurant(id, entity);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityToApi(updated));
    }

}
