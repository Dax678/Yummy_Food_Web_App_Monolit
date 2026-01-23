package org.yummyfood.backend.service;

import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.dto.request.MenuItemRequest;

import java.util.Set;
import java.util.UUID;

public interface MenuItemService {
    MenuItem getMenuItemById(UUID id);
    MenuItem createMenuItem(Restaurant restaurant, MenuItemRequest request);
    MenuItem updateMenuItem(UUID id, MenuItem menuItem);
    Set<MenuItem> listByRestaurantId(UUID restaurantId);
}
