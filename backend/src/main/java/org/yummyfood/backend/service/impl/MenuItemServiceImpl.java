package org.yummyfood.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.repository.MenuItemRepository;
import org.yummyfood.backend.repository.RestaurantRepository;
import org.yummyfood.backend.service.MenuItemService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(UUID id) {
        return getMenuItemOrElseThrow(id);
    }

    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        UUID restaurantId = menuItem.getRestaurant().getId();
        Restaurant restaurant = getRestaurantOrElseThrow(restaurantId);

        menuItem.setRestaurant(restaurant);

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
    public List<MenuItem> listByRestaurantId(UUID restaurantId) {
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
}
