package com.ecommerce.order.adapter.out.messaging;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ecommerce.order.adapter.out.messaging.event.OrderEventMessage;
import com.ecommerce.order.adapter.out.messaging.event.OrderEventPayload;
import com.ecommerce.order.adapter.out.messaging.event.OrderEventType;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.ports.out.OrderEventPublisherPort;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String orderEventsTopic;

    public KafkaOrderEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.topics.order-events:ecommerce.order.events.v1}") String orderEventsTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderEventsTopic = orderEventsTopic;
    }

    @Override
    public void publishOrderEvent(OrderEventType eventType, Order order, String reason, String correlationId) {
        String partitionKey = resolvePartitionKey(order);
        OrderEventMessage message = buildMessage(eventType, order, reason, correlationId, partitionKey);

        kafkaTemplate.send(orderEventsTopic, partitionKey, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        LOGGER.error("Failed publish eventId={} eventType={} correlationId={} topic={} partitionKey={} error={}",
                                message.getEventId(),
                                message.getEventType(),
                                message.getCorrelationId(),
                                orderEventsTopic,
                                partitionKey,
                                ex.getMessage());
                        return;
                    }

                    LOGGER.info("Published eventId={} eventType={} correlationId={} topic={} partition={} offset={}",
                            message.getEventId(),
                            message.getEventType(),
                            message.getCorrelationId(),
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset());
                });
    }

    private OrderEventMessage buildMessage(OrderEventType eventType,
            Order order,
            String reason,
            String correlationId,
            String partitionKey) {
        OrderEventMessage message = new OrderEventMessage();
        message.setEventId(UUID.randomUUID().toString());
        message.setEventType(eventType.value());
        message.setEventVersion("1.0");
        message.setOccurredAt(OffsetDateTime.now(ZoneOffset.UTC));
        message.setProducer("order-service");
        message.setTraceId(UUID.randomUUID().toString());
        message.setCorrelationId(Optional.ofNullable(correlationId)
                .filter(c -> !c.isBlank())
                .orElseGet(() -> Optional.ofNullable(order.getOrderNumber()).orElse(partitionKey)));
        message.setPartitionKey(partitionKey);
        message.setPayload(buildPayload(order, reason));
        return message;
    }

    private OrderEventPayload buildPayload(Order order, String reason) {
        OrderEventPayload payload = new OrderEventPayload();
        payload.setOrderId(order.getId());
        payload.setOrderNumber(order.getOrderNumber());
        payload.setCustomerId(order.getCustomerId());
        payload.setProductId(order.getProductId());
        payload.setPaymentId(order.getPaymentId());
        payload.setStatus(order.getStatus());
        payload.setReason(reason);

        if (order.getFinancials() != null) {
            payload.setTotalAmount(order.getFinancials().getTotalAmount());
            payload.setCurrency(order.getFinancials().getCurrency());
        }

        if (order.getShippingInfo() != null) {
            payload.setTrackingNumber(order.getShippingInfo().getTrackingNumber());
            payload.setShippingMethod(order.getShippingInfo().getShippingMethod());
        }

        return payload;
    }

    private String resolvePartitionKey(Order order) {
        if (order.getId() != null) {
            return String.valueOf(order.getId());
        }
        if (order.getOrderNumber() != null && !order.getOrderNumber().isBlank()) {
            return order.getOrderNumber();
        }
        return UUID.randomUUID().toString();
    }
}
