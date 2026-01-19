package org.yummyfood.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.yummyfood.backend.domain.*;
import org.yummyfood.backend.dto.request.CreateOrderRequest;
import org.yummyfood.backend.dto.response.OrderResponse;
import org.yummyfood.backend.exception.NotFoundException;
import org.yummyfood.backend.exception.UserNotFoundException;
import org.yummyfood.backend.mapper.OrderMapper;
import org.yummyfood.backend.repository.UserRepository;
import org.yummyfood.backend.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        User user = getCurrentUser();

        Order order = Order.builder()
                .user(user)
                .restaurant(Restaurant.builder().id(request.restaurantId()).build())
                .notes(request.notes())
                .build();

        for(CreateOrderRequest.OrderItemRequest item : request.items()) {
            OrderItem orderItem = OrderItem.builder()
                    .menuItem(MenuItem.builder().id(item.menuItemId()).build())
                    .quantity(item.quantity())
                    .build();
            order.addItem(orderItem);
        }

        Order saved = orderService.createOrder(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderMapper.toResponse(saved));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> listMyOrders() {
        User user = getCurrentUser();

        List<OrderResponse> response = orderService.listByUserId(user.getId()).stream()
                .map(orderMapper::toResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication.getName() == null) {
            throw new NotFoundException("User not authenticated");
        }
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authentication.getName()));
    }
}
