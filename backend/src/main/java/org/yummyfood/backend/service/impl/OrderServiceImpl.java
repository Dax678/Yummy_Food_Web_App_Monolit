package org.yummyfood.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yummyfood.backend.domain.*;
import org.yummyfood.backend.domain.model.OrderStatus;
import org.yummyfood.backend.exception.InvalidInputException;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.repository.MenuItemRepository;
import org.yummyfood.backend.repository.OrderRepository;
import org.yummyfood.backend.repository.RestaurantRepository;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(UUID id) {
        return getOrderOrElseThrow(id);
    }

    @Override
    public Order createOrder(Order order) {
        User user = userRepository.findById(order.getUser().getId()).orElseThrow(
                () -> new NotFoundException("User not found with id: " + order.getUser().getId())
        );

        Restaurant restaurant = restaurantRepository.findById(order.getRestaurant().getId()).orElseThrow(
                () -> new NotFoundException("Restaurant not found with id: " + order.getRestaurant().getId())
        );

        Order newOrder =Order.builder()
                .user(user)
                .restaurant(restaurant)
                .status(order.getStatus())
                .itemsTotal(BigDecimal.ZERO)
                .deliveryFee(BigDecimal.ZERO)
                .grandTotal(BigDecimal.ZERO)
                .notes(order.getNotes())
                .build();

        for(OrderItem orderItem : order.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(orderItem.getMenuItem().getId()).orElseThrow(
                    () -> new NotFoundException("MenuItem not found with id: " + orderItem.getMenuItem().getId())
            );

            BigDecimal unitPrice = orderItem.getUnitPrice();
            BigDecimal line = unitPrice.multiply(new BigDecimal(orderItem.getQuantity()));


        }

        return orderRepository.save(newOrder);
    }

    @Override
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order updatedOrder = getOrderOrElseThrow(id);
        updatedOrder.setStatus(status);

        return orderRepository.save(updatedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listByUserId(UUID userId) {
        return orderRepository.findAllByUser_Id(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listByRestaurantId(UUID restaurantId) {
        return List.of();
    }

    private Order getOrderOrElseThrow(UUID id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order not found: " + id)
        );
    }
}
