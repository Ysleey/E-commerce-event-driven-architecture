package com.ecommerce.order.ports.out;

import com.ecommerce.order.adapter.out.messaging.event.OrderEventType;
import com.ecommerce.order.domain.model.Order;

public interface OrderEventPublisherPort {

    void publishOrderEvent(OrderEventType eventType, Order order, String reason, String correlationId);
}
