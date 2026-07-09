package com.ecommerce.shipping.adapter.out.persistence;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import com.ecommerce.shipping.application.ShippingServiceApplication;
import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;

@DataJpaTest
@ContextConfiguration(classes = ShippingServiceApplication.class)
@Import(ShippingPersistenceAdapter.class)
class ShippingPersistenceAdapterTest {

    @Autowired
    private ShippingPersistenceAdapter shippingPersistenceAdapter;

    @Test
    void shouldPersistShipmentWithInitialPendingStatus() {
        UUID orderId = UUID.randomUUID();
        Shipping shipping = Shipping.builder()
                .id(UUID.randomUUID())
                .orderId(orderId)
                .status(ShippingStatus.PENDING)
                .build();

        Shipping saved = shippingPersistenceAdapter.save(shipping);

        assertNotNull(saved.getId());
        assertEquals(orderId, saved.getOrderId());
        assertEquals(ShippingStatus.PENDING, saved.getStatus());
    }
}
