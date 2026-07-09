package com.ecommerce.shipping.application.service;

import java.time.OffsetDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.in.CreateShippingUseCase;
import com.ecommerce.shipping.ports.in.UpdateShippingStatusUseCase;
import com.ecommerce.shipping.ports.out.ShippingEventPublisherPort;
import com.ecommerce.shipping.ports.out.ShippingRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingUseCaseService implements CreateShippingUseCase, UpdateShippingStatusUseCase {

	private final ShippingRepositoryPort shippingRepositoryPort;
	private final ShippingEventPublisherPort shippingEventPublisherPort;

	@Override
	public Shipping createPendingShipment(Long orderId, String correlationId) {
		OffsetDateTime now = OffsetDateTime.now();

		Shipping shipping = Shipping.builder()
				.id(UUID.randomUUID())
				.orderId(orderId)
				.status(ShippingStatus.PENDING)
				.createdAt(now)
				.updatedAt(now)
				.build();

		Shipping savedShipping = shippingRepositoryPort.save(shipping);
		shippingEventPublisherPort.publishShippingCreated(savedShipping, correlationId);
		return savedShipping;
	}

	@Override
	public Shipping updateShippingStatus(Long orderId, ShippingStatus newStatus, String trackingNumber, String correlationId) {
		Shipping existingShipping = shippingRepositoryPort.findByOrderId(orderId)
				.orElseThrow(() -> new NoSuchElementException("Shipping not found for orderId=" + orderId));
		ShippingStatus previousStatus = existingShipping.getStatus();
		OffsetDateTime now = OffsetDateTime.now();

		Shipping updatedShipping = Shipping.builder()
				.id(existingShipping.getId())
				.orderId(existingShipping.getOrderId())
				.status(newStatus)
				.trackingNumber(trackingNumber)
				.createdAt(existingShipping.getCreatedAt())
				.updatedAt(now)
				.build();

		Shipping savedShipping = shippingRepositoryPort.save(updatedShipping);
		shippingEventPublisherPort.publishShippingStatusChanged(savedShipping, previousStatus, correlationId);
		return savedShipping;
	}
}
