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

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;

    @PostMapping
    public ResponseEntity<MenuItemDetailsResponse> createMenuItem(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        MenuItem menuItem = MenuItem.builder()
                .restaurant(Restaurant.builder().id(restaurantId).build())
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .isAvailable(request.isAvailable())
                .imageUrl(request.imageUrl())
                .build();

        MenuItem created = menuItemService.createMenuItem(menuItem);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(menuItemMapper.toDetailsResponse(created));
    }
}
