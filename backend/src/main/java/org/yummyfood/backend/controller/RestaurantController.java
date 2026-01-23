package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.domain.model.UserRole;
import org.yummyfood.backend.dto.request.RestaurantRequest;
import org.yummyfood.backend.dto.response.RestaurantDetailsResponse;
import org.yummyfood.backend.dto.response.RestaurantResponse;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.exception.UserNotFoundException;
import org.yummyfood.backend.mapper.MenuItemMapper;
import org.yummyfood.backend.mapper.RestaurantMapper;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.MenuItemService;
import org.yummyfood.backend.service.RestaurantService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<RestaurantDetailsResponse> create(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant saved = restaurantService.createRestaurant(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantMapper.entityToApi(saved));
    }

    @PostMapping("/me")
    public ResponseEntity<RestaurantDetailsResponse> addMyRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        User user = getCurrentUser();
        requireRestaurantOwner(user);

        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        entity.setOwner(user);
        Restaurant saved = restaurantService.createRestaurant(entity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(restaurantMapper.entityToApi(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDetailsResponse> update(@PathVariable UUID id, @Valid @RequestBody RestaurantRequest restaurantRequest) {
        User user = getCurrentUser();
        requireRestaurantOwner(user);

        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(user.getId())) {
            throw new InvalidInputException("Restaurant does not belong to current user.");
        }

        Restaurant entity = restaurantMapper.apiToEntity(restaurantRequest);
        Restaurant updated = restaurantService.updateRestaurant(id, entity);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantMapper.entityToApi(updated));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getRestaurants(
            @RequestParam(required = false, defaultValue = "true") boolean isActive,
            @RequestParam(required = false, defaultValue = "") String search
    ) {
        List<Restaurant> restaurantList = restaurantService.searchByNameAndActive(search, isActive);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantList.stream()
                        .map(restaurant ->
                                new RestaurantResponse(
                                        restaurant.getId(),
                                        restaurant.getName(),
                                        restaurant.getDescription(),
                                        restaurant.getAvgRating(),
                                        menuItemService.listByRestaurantId(restaurant.getId()).stream()
                                                .map(menuItemMapper::toRestaurantMenuItemResponse).collect(Collectors.toSet()))
                        ).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantDetailsById(@PathVariable UUID id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        var items = menuItemService.listByRestaurantId(restaurant.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RestaurantDetailsResponse(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getDescription(),
                        restaurant.getPhone(),
                        restaurant.getEmail(),
                        restaurant.getAvgRating(),
                        restaurant.isActive(),
                        items.stream()
                                .map(menuItemMapper::toRestaurantMenuItemResponse)
                                .collect(Collectors.toList())
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<List<RestaurantResponse>> getMyRestaurants() {
        User user = getCurrentUser();
        List<Restaurant> restaurants = restaurantService.listRestaurantsByOwnerId(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurants.stream().map(restaurant -> {
                                    var items = menuItemService.listByRestaurantId(restaurant.getId());
                                    return new RestaurantResponse(
                                            restaurant.getId(),
                                            restaurant.getName(),
                                            restaurant.getDescription(),
                                            restaurant.getAvgRating(),
                                            items.stream()
                                                    .map(menuItemMapper::toRestaurantMenuItemResponse)
                                                    .collect(Collectors.toSet())
                                    );
                                }
                        ).collect(Collectors.toList())
                );
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new NotFoundException("User not authenticated");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authentication.getName()));
    }

    private void requireRestaurantOwner(User user) {
        if (user.getRole() != UserRole.RESTAURANT) {
            throw new InvalidInputException("User is not restaurant owner.");
        }
    }
}
