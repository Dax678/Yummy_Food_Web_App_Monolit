package org.yummyfood.backend.service;

import org.yummyfood.backend.domain.MenuItem;

import java.util.List;
import java.util.UUID;

public interface MenuItemService {
    MenuItem getMenuItemById(UUID id);
    MenuItem createMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(UUID id, MenuItem menuItem);
    List<MenuItem> listByRestaurantId(UUID restaurantId);
}
