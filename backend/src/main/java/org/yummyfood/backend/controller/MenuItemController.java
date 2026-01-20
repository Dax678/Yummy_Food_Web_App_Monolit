package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.domain.model.UserRole;
import org.yummyfood.backend.dto.request.MenuItemRequest;
import org.yummyfood.backend.dto.response.MenuItemDetailsResponse;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.exception.UserNotFoundException;
import org.yummyfood.backend.mapper.MenuItemMapper;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.MenuItemService;
import org.yummyfood.backend.service.RestaurantService;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final RestaurantService restaurantService;
    private final MenuItemMapper menuItemMapper;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<MenuItemDetailsResponse> createMenuItem(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody MenuItemRequest request
    ) {
        User user = getCurrentUser();
        requireRestaurantOwner(user);

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        if(restaurant.getOwner() == null || restaurant.getOwner().getId().equals(user.getId())) {
            throw new InvalidInputException("Restaurant does not belong to current user.");
        }

        MenuItem menuItem = MenuItem.builder()
                .restaurant(restaurant)
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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new NotFoundException("User not authenticated");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authentication.getName()));
    }

    private void requireRestaurantOwner(User user) {
        if(user.getRole() != UserRole.RESTAURANT) {
            throw new InvalidInputException("User is not restaurant owner.");
        }
    }
}
