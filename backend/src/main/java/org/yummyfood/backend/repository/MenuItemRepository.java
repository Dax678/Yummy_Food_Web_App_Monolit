package org.yummyfood.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yummyfood.backend.domain.MenuItem;
import org.yummyfood.backend.domain.Restaurant;

import java.util.Set;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    Set<MenuItem> findByRestaurant(Restaurant restaurant);
}
