package com.ecommerce.shipping.adapter.in.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.ecommerce.shipping.adapter.in.messaging.event.OrderEventMessage;
import com.ecommerce.shipping.application.service.KafkaMessageDeduplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderEventsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventsConsumer.class);

    private final ObjectMapper objectMapper;
    private final KafkaMessageDeduplicationService deduplicationService;
    private final String consumerName;

    public OrderEventsConsumer(
            ObjectMapper objectMapper,
            KafkaMessageDeduplicationService deduplicationService,
            @Value("${app.kafka.consumer.name:shipping-order-events-consumer-v1}") String consumerName) {
        this.objectMapper = objectMapper;
        this.deduplicationService = deduplicationService;
        this.consumerName = consumerName;
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

            if (!isSupportedContract(message)) {
                LOGGER.warn(
                        "Rejected event by contract validation eventId={} eventType={} eventVersion={} topic={} partition={} offset={} key={}",
                        message.getEventId(),
                        message.getEventType(),
                        message.getEventVersion(),
                        topic,
                        partition,
                        offset,
                        key);
                return;
            }

            if (!deduplicationService.registerIfFirstProcessing(message.getEventId(), consumerName)) {
                LOGGER.info(
                        "Skipped duplicate eventId={} consumerName={} topic={} partition={} offset={} key={}",
                        message.getEventId(),
                        consumerName,
                        topic,
                        partition,
                        offset,
                        key);
                return;
            }

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

    private boolean isSupportedContract(OrderEventMessage message) {
        if (message == null) {
            return false;
        }
        if (isBlank(message.getEventId()) || isBlank(message.getEventType()) || isBlank(message.getEventVersion())) {
            return false;
        }
        if (!"OrderCreated".equalsIgnoreCase(message.getEventType())) {
            return false;
        }
        if (!message.getEventVersion().startsWith("1")) {
            return false;
        }
        return message.getPayload() != null && message.getPayload().getOrderId() != null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
