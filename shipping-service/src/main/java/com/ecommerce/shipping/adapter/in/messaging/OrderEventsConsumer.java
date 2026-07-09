package com.ecommerce.shipping.adapter.in.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderEventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventsConsumer.class);

    private final ObjectMapper objectMapper;

    public OrderEventsConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.kafka.topics.order-events:ecommerce.order.events.v1}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(
            String rawMessage,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        try {
            OrderEventMessage message = objectMapper.readValue(rawMessage, OrderEventMessage.class);

            LOGGER.info(
                    "Consumed order eventId={} eventType={} correlationId={} topic={} partition={} offset={} key={}",
                    message.getEventId(),
                    message.getEventType(),
                    message.getCorrelationId(),
                    topic,
                    partition,
                    offset,
                    key);

            if ("OrderCreated".equalsIgnoreCase(message.getEventType())) {
                LOGGER.info(
                        "Base consumer ready for KAN-16: received OrderCreated orderId={} orderNumber={}",
                        message.getPayload() != null ? message.getPayload().getOrderId() : null,
                        message.getPayload() != null ? message.getPayload().getOrderNumber() : null);
            }

        } catch (Exception ex) {
            LOGGER.error(
                    "Failed to parse message topic={} partition={} offset={} key={} error={}",
                    topic,
                    partition,
                    offset,
                    key,
                    ex.getMessage());
        }
    }
}
