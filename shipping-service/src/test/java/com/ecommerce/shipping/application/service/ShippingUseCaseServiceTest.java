package com.ecommerce.shipping.application.service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.out.ShippingEventPublisherPort;
import com.ecommerce.shipping.ports.out.ShippingRepositoryPort;

class ShippingUseCaseServiceTest {

	@Test
	void shouldCreatePendingShipmentWithInitialPendingStatus() {
		ShippingRepositoryPort repositoryPort = mock(ShippingRepositoryPort.class);
		ShippingEventPublisherPort shippingEventPublisherPort = mock(ShippingEventPublisherPort.class);
		when(repositoryPort.save(any(Shipping.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ShippingUseCaseService service = new ShippingUseCaseService(repositoryPort, shippingEventPublisherPort);
		Long orderId = 42L;

		Shipping result = service.createPendingShipment(orderId, "corr-42");

		assertNotNull(result.getId());
		assertEquals(orderId, result.getOrderId());
		assertEquals(ShippingStatus.PENDING, result.getStatus());
		assertNull(result.getTrackingNumber());
		verify(repositoryPort).save(any(Shipping.class));
		verify(shippingEventPublisherPort).publishShippingCreated(any(Shipping.class), eq("corr-42"));
	}

	@Test
	void shouldUpdateShippingStatusAndPublishStatusChangedEvent() {
		ShippingRepositoryPort repositoryPort = mock(ShippingRepositoryPort.class);
		ShippingEventPublisherPort shippingEventPublisherPort = mock(ShippingEventPublisherPort.class);
		Shipping existingShipping = Shipping.builder()
				.id(UUID.randomUUID())
				.orderId(42L)
				.status(ShippingStatus.PENDING)
				.createdAt(OffsetDateTime.now().minusHours(1))
				.updatedAt(OffsetDateTime.now().minusHours(1))
				.build();

		when(repositoryPort.findByOrderId(42L)).thenReturn(Optional.of(existingShipping));
		when(repositoryPort.save(any(Shipping.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ShippingUseCaseService service = new ShippingUseCaseService(repositoryPort, shippingEventPublisherPort);

		Shipping result = service.updateShippingStatus(42L, ShippingStatus.SHIPPED, "TRK-42", "corr-42");

		assertEquals(ShippingStatus.SHIPPED, result.getStatus());
		assertEquals("TRK-42", result.getTrackingNumber());
		assertEquals(42L, result.getOrderId());
		verify(shippingEventPublisherPort).publishShippingStatusChanged(any(Shipping.class), eq(ShippingStatus.PENDING), eq("corr-42"));
	}
}
