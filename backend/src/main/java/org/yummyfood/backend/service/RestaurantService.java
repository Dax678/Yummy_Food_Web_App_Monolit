package org.yummyfood.backend.service;

import org.yummyfood.backend.domain.Restaurant;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {
    Restaurant getRestaurantById(UUID id);
    List<Restaurant> listRestaurantsByOwnerId(UUID ownerId);
    Restaurant createRestaurant(Restaurant restaurant);
    Restaurant updateRestaurant(UUID id, Restaurant restaurant);
    List<Restaurant> searchByNameAndActive(String name, boolean active);
}
