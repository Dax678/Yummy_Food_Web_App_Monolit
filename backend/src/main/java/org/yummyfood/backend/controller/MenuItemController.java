package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.MenuItemRequest;
import org.yummyfood.backend.dto.response.MenuItemDetailsResponse;
import org.yummyfood.backend.mapper.MenuItemMapper;
import org.yummyfood.backend.service.MenuItemService;
import org.yummyfood.backend.service.RestaurantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;
    private final RestaurantService restaurantService;


    @PostMapping
    public ResponseEntity<MenuItemDetailsResponse> createMenuItem(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        MenuItem created = menuItemService.createMenuItem(restaurant, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(menuItemMapper.toDetailsResponse(created));
    }
}
