package org.yummyfood.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.yummyfood.backend.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status = OrderStatus.NEW;

    @Column(name = "items_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal itemsTotal = BigDecimal.ZERO;

    @Column(name = "delivery_fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal deliveryFee = BigDecimal.ZERO;

    @Column(name = "grand_total", precision = 10, scale = 2, nullable = false)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Lob
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderItem> items = new LinkedHashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Payment payment;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void attachPayment(Payment payment) {
        this.payment = payment;
        if (payment != null) payment.setOrder(this);
    }
}
