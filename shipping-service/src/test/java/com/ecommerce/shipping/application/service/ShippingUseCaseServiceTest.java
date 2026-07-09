package com.ecommerce.shipping.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.out.ShippingRepositoryPort;

class ShippingUseCaseServiceTest {

    @Test
    void shouldCreatePendingShipmentWithInitialPendingStatus() {
        ShippingRepositoryPort repositoryPort = mock(ShippingRepositoryPort.class);
        when(repositoryPort.save(org.mockito.ArgumentMatchers.any(Shipping.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ShippingUseCaseService service = new ShippingUseCaseService(repositoryPort);
        Long orderId = 42L;

        Shipping result = service.createPendingShipment(orderId);

        assertNotNull(result.getId());
        assertEquals(orderId, result.getOrderId());
        assertEquals(ShippingStatus.PENDING, result.getStatus());
        assertNull(result.getTrackingNumber());
        verify(repositoryPort).save(org.mockito.ArgumentMatchers.any(Shipping.class));
    }
}
