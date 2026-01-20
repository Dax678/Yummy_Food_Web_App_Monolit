package org.yummyfood.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yummyfood.backend.domain.Restaurant;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findByIsActiveAndNameContainingIgnoreCase(boolean active, String name);
    List<Restaurant> findByIsActive(boolean active);
    List<Restaurant> findAllByOwner_id(UUID ownerId);
}
