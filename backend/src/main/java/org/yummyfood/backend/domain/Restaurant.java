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
@Table(name = "restaurants")
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Lob
    private String description;

    @Column(length = 32)
    private String phone;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "avg_rating", precision = 3, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal avgRating = BigDecimal.ZERO;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MenuItem> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();
}
