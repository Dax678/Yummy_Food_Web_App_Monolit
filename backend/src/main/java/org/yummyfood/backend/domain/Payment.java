package org.yummyfood.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.yummyfood.backend.domain.model.PaymentMethod;
import org.yummyfood.backend.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(length = 80)
    private String provider;

    @Column(name = "provider_ref", length = 120)
    private String providerRef;
}
