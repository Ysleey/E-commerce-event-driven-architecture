package com.ecommerce.shipping.ports.out;

import java.util.Optional;
import java.util.UUID;

import com.ecommerce.shipping.domain.model.Shipping;

public interface ShippingRepositoryPort {
	Shipping save(Shipping shipping);
	Optional<Shipping> findByOrderId(Long orderId);
	Optional<Shipping> findById(UUID id);
}
