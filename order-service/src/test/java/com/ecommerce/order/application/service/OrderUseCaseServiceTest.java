package com.ecommerce.order.application.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.order.adapter.out.messaging.event.OrderEventType;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.ports.out.OrderEventPublisherPort;
import com.ecommerce.order.ports.out.OrderListRepositoryPort;
import com.ecommerce.order.ports.out.OrderQueryRepositoryPort;
import com.ecommerce.order.ports.out.OrderRepositoryPort;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseServiceTest {

    @Mock
    private OrderRepositoryPort repositoryPort;

    @Mock
    private OrderQueryRepositoryPort queryRepositoryPort;

    @Mock
    private OrderListRepositoryPort listRepositoryPort;

    @Mock
    private OrderEventPublisherPort eventPublisherPort;

    @InjectMocks
    private OrderUseCaseService service;

    @Test
    void createOrderShouldPublishOrderCreatedEvent() {
        Order input = Order.builder().orderNumber("ORD-100").status("").build();
        Order saved = Order.builder().id(1L).orderNumber("ORD-100").status("CREATED").build();

        when(repositoryPort.save(input)).thenReturn(saved);

        Order result = service.createOrder(input);

        assertEquals("CREATED", result.getStatus());
        verify(eventPublisherPort).publishOrderEvent(eq(OrderEventType.ORDER_CREATED), eq(saved), isNull(),
                eq("ORD-100"));
    }

    @Test
    void createOrderShouldGenerateOrderNumberWhenMissing() {
        Order input = Order.builder().status("CREATED").build();
        Order saved = Order.builder().id(2L).status("CREATED").build();

        when(repositoryPort.save(input)).thenAnswer(invocation -> {
            Order toSave = invocation.getArgument(0);
            saved.setOrderNumber(toSave.getOrderNumber());
            return saved;
        });

        Order result = service.createOrder(input);

        assertNotNull(result.getOrderNumber());
        verify(eventPublisherPort).publishOrderEvent(eq(OrderEventType.ORDER_CREATED), eq(saved), isNull(),
                anyString());
    }

    @Test
    void shipOrderShouldPublishOrderShippedEvent() {
        Order existing = Order.builder().id(10L).orderNumber("ORD-200").status("CREATED").build();
        Order saved = Order.builder().id(10L).orderNumber("ORD-200").status("SHIPPED").build();

        when(queryRepositoryPort.findById(10L)).thenReturn(Optional.of(existing));
        when(repositoryPort.save(existing)).thenReturn(saved);

        Order result = service.shipOrder(10L, "TRK-001");

        assertEquals("SHIPPED", result.getStatus());
        verify(eventPublisherPort).publishOrderEvent(eq(OrderEventType.ORDER_SHIPPED), eq(saved), isNull(),
            eq("ORD-200"));
    }

    @Test
    void refundOrderShouldPublishOrderRefundRequestedEvent() {
        Order existing = Order.builder().id(20L).orderNumber("ORD-300").status("COMPLETED").build();
        Order saved = Order.builder().id(20L).orderNumber("ORD-300").status("REFUND_REQUESTED").build();

        when(queryRepositoryPort.findById(20L)).thenReturn(Optional.of(existing));
        when(repositoryPort.save(existing)).thenReturn(saved);

        Order result = service.refundOrder(20L);

        assertEquals("REFUND_REQUESTED", result.getStatus());
        verify(eventPublisherPort).publishOrderEvent(eq(OrderEventType.ORDER_REFUND_REQUESTED), eq(saved), isNull(),
            eq("ORD-300"));
    }
}
