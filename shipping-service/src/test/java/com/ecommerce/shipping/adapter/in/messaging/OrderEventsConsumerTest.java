package com.ecommerce.shipping.adapter.in.messaging;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventMessage;
import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventPayload;
import com.ecommerce.shipping.application.service.KafkaMessageDeduplicationService;
import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.in.CreateShippingUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;

class OrderEventsConsumerTest {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void shouldCreatePendingShippingWhenProcessingFirstOrderCreatedEvent() throws Exception {
        KafkaMessageDeduplicationService deduplicationService = mock(KafkaMessageDeduplicationService.class);
        CreateShippingUseCase createShippingUseCase = mock(CreateShippingUseCase.class);

        when(deduplicationService.registerIfFirstProcessing("event-123", "shipping-order-events-consumer-v1"))
                .thenReturn(true);
        when(createShippingUseCase.createPendingShipment(42L, null)).thenReturn(Shipping.builder()
                .id(UUID.randomUUID())
                .orderId(42L)
                .status(ShippingStatus.PENDING)
                .build());

        OrderEventsConsumer consumer = new OrderEventsConsumer(
                objectMapper,
                deduplicationService,
                createShippingUseCase,
                "shipping-order-events-consumer-v1");

        consumer.consume(buildOrderCreatedEventJson(), "ecommerce.order.events.v1", 1, 10L, "42");

        verify(createShippingUseCase).createPendingShipment(42L, null);
    }

    @Test
    void shouldSkipDuplicateEventWithoutCreatingShipping() throws Exception {
        KafkaMessageDeduplicationService deduplicationService = mock(KafkaMessageDeduplicationService.class);
        CreateShippingUseCase createShippingUseCase = mock(CreateShippingUseCase.class);

        when(deduplicationService.registerIfFirstProcessing("event-123", "shipping-order-events-consumer-v1"))
                .thenReturn(false);

        OrderEventsConsumer consumer = new OrderEventsConsumer(
                objectMapper,
                deduplicationService,
                createShippingUseCase,
                "shipping-order-events-consumer-v1");

        consumer.consume(buildOrderCreatedEventJson(), "ecommerce.order.events.v1", 1, 10L, "42");

        verify(createShippingUseCase, never()).createPendingShipment(42L, null);
    }

    private String buildOrderCreatedEventJson() throws Exception {
        OrderEventPayload payload = new OrderEventPayload();
        payload.setOrderId(42L);
        payload.setOrderNumber("ORD-42");

        OrderEventMessage message = new OrderEventMessage();
        message.setEventId("event-123");
        message.setEventType("OrderCreated");
        message.setEventVersion("1.0");
        message.setCorrelationId(null);
        message.setPayload(payload);

        return objectMapper.writeValueAsString(message);
    }
}