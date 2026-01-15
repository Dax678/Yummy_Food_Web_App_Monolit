package org.yummyfood.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "menu_items", uniqueConstraints = @UniqueConstraint(columnNames = {"restaurant_id", "name"}))
public class MenuItem extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 150)
    private String name;

    @Lob
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();
}
