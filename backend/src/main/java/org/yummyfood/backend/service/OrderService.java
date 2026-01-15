package org.yummyfood.backend.service;


import org.yummyfood.backend.domain.Order;
import org.yummyfood.backend.domain.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order getOrderById(UUID id);
    Order createOrder(Order order);
    Order updateOrderStatus(UUID id, OrderStatus status);
    List<Order> listByUserId(UUID userId);
    List<Order> listByRestaurantId(UUID restaurantId);
}
