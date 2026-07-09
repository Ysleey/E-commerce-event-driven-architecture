package com.ecommerce.shipping.application.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.in.CreateShippingUseCase;
import com.ecommerce.shipping.ports.out.ShippingRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingUseCaseService implements CreateShippingUseCase {

	private final ShippingRepositoryPort shippingRepositoryPort;

	@Override
	public Shipping createPendingShipment(UUID orderId) {
		OffsetDateTime now = OffsetDateTime.now();

		Shipping shipping = Shipping.builder()
				.id(UUID.randomUUID())
				.orderId(orderId)
				.status(ShippingStatus.PENDING)
				.createdAt(now)
				.updatedAt(now)
				.build();

		return shippingRepositoryPort.save(shipping);
	}
}
