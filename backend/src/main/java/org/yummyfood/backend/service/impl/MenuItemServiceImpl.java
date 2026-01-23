package org.yummyfood.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.domain.User;
import org.yummyfood.backend.dto.request.MenuItemRequest;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.exception.UserNotFoundException;
import org.yummyfood.backend.repository.MenuItemRepository;
import org.yummyfood.backend.repository.RestaurantRepository;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.MenuItemService;
import org.yummyfood.backend.service.RestaurantService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(UUID id) {
        return getMenuItemOrElseThrow(id);
    }

    @Override
    public MenuItem createMenuItem(Restaurant restaurant, MenuItemRequest request) {
        User user = getCurrentUser();
        requireRestaurantOwner(user, restaurant);

        MenuItem menuItem = MenuItem.builder()
                .restaurant(restaurant)
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .isAvailable(request.isAvailable())
                .imageUrl(request.imageUrl())
                .build();

        return menuItemRepository.save(menuItem);
    }

    @Override
    public MenuItem updateMenuItem(UUID id, MenuItem menuItem) {
        MenuItem updated =  getMenuItemOrElseThrow(id);
        updated.setName(menuItem.getName());
        updated.setDescription(menuItem.getDescription());
        updated.setPrice(menuItem.getPrice());
        updated.setAvailable(menuItem.isAvailable());
        updated.setImageUrl(menuItem.getImageUrl());

        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<MenuItem> listByRestaurantId(UUID restaurantId) {
        Restaurant restaurant = getRestaurantOrElseThrow(restaurantId);

        return menuItemRepository.findByRestaurant(restaurant);
    }

    private Restaurant getRestaurantOrElseThrow(UUID restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant not found: " + restaurantId));
    }

    private MenuItem getMenuItemOrElseThrow(UUID menuItemId) {
        return menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new NotFoundException("MenuItem not found: " + menuItemId));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new NotFoundException("User not authenticated");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authentication.getName()));
    }

    private void requireRestaurantOwner(User user, Restaurant restaurant) {
        if(restaurant.getOwner() == null || !restaurant.getOwner().getId().equals(user.getId())) {
            throw new InvalidInputException("Restaurant does not belong to current user.");
        }
    }
}
