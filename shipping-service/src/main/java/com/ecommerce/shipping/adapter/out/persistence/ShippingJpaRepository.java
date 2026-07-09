package com.ecommerce.shipping.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingJpaRepository extends JpaRepository<ShippingEntity, UUID> {
	Optional<ShippingEntity> findByOrderId(Long orderId);
}
