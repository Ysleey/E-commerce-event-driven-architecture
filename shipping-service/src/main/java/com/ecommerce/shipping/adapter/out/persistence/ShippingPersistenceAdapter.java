package com.ecommerce.shipping.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.ports.out.ShippingRepositoryPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShippingPersistenceAdapter implements ShippingRepositoryPort {

	private final ShippingJpaRepository shippingJpaRepository;

	@Override
	public Shipping save(Shipping shipping) {
		ShippingEntity entity = toEntity(shipping);
		ShippingEntity persisted = shippingJpaRepository.save(entity);
		return toDomain(persisted);
	}

	private ShippingEntity toEntity(Shipping shipping) {
		ShippingEntity entity = new ShippingEntity();
		entity.setId(shipping.getId());
		entity.setOrderId(shipping.getOrderId());
		entity.setStatus(shipping.getStatus());
		entity.setTrackingNumber(shipping.getTrackingNumber());
		entity.setCreatedAt(shipping.getCreatedAt());
		entity.setUpdatedAt(shipping.getUpdatedAt());
		return entity;
	}

	private Shipping toDomain(ShippingEntity entity) {
		return Shipping.builder()
				.id(entity.getId())
				.orderId(entity.getOrderId())
				.status(entity.getStatus())
				.trackingNumber(entity.getTrackingNumber())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}
