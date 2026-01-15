package org.yummyfood.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yummyfood.backend.domain.Restaurant;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.repository.RestaurantRepository;
import org.yummyfood.backend.service.RestaurantService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public Restaurant getRestaurantById(UUID id) {
        return getRestaurantOrElseThrow(id);
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        try {
            return restaurantRepository.save(restaurant);
        } catch (DuplicateKeyException e) {
            throw new InvalidInputException("Duplicate Key, restaurantId");
        }
    }

    @Override
    public Restaurant updateRestaurant(UUID id, Restaurant restaurant) {
        Restaurant entity = getRestaurantOrElseThrow(id);

        entity.setName(restaurant.getName());
        entity.setDescription(restaurant.getDescription());
        entity.setPhone(restaurant.getPhone());
        entity.setEmail(restaurant.getEmail());
        entity.setActive(restaurant.isActive());

        return restaurantRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> listActive(boolean active) {
        return restaurantRepository.findByIsActive(active);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Restaurant> searchByNameAndActive(String name, boolean active) {
        return restaurantRepository.findByIsActiveAndNameContainingIgnoreCase(active, name);
    }

    private Restaurant getRestaurantOrElseThrow(UUID id) {
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Restaurant not found: " + id));
    }
}
