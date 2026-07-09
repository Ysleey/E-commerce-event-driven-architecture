package com.ecommerce.shipping.adapter.out.messaging;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ecommerce.shipping.adapter.out.messaging.event.ShippingEventMessage;
import com.ecommerce.shipping.adapter.out.messaging.event.ShippingEventPayload;
import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;
import com.ecommerce.shipping.ports.out.ShippingEventPublisherPort;

@Component
public class KafkaShippingEventPublisher implements ShippingEventPublisherPort {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaShippingEventPublisher.class);

	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final String shippingEventsTopic;

	public KafkaShippingEventPublisher(
			KafkaTemplate<String, Object> kafkaTemplate,
			@Value("${app.kafka.topics.shipping-events:ecommerce.shipping.events.v1}") String shippingEventsTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.shippingEventsTopic = shippingEventsTopic;
	}

	@Override
	public void publishShippingCreated(Shipping shipping, String correlationId) {
		publishEvent("ShippingCreated", shipping, null, correlationId);
	}

	@Override
	public void publishShippingStatusChanged(Shipping shipping, ShippingStatus previousStatus, String correlationId) {
		publishEvent("ShippingStatusChanged", shipping, previousStatus != null ? previousStatus.name() : null, correlationId);
	}

	private void publishEvent(String eventType, Shipping shipping, String previousStatus, String correlationId) {
		String partitionKey = resolvePartitionKey(shipping);
		ShippingEventMessage message = buildMessage(eventType, shipping, previousStatus, correlationId, partitionKey);

		kafkaTemplate.send(shippingEventsTopic, partitionKey, message)
				.whenComplete((result, ex) -> {
					if (ex != null) {
						LOGGER.error("Failed publish shipping eventId={} eventType={} correlationId={} topic={} partitionKey={} error={}",
								message.getEventId(),
								message.getEventType(),
								message.getCorrelationId(),
								shippingEventsTopic,
								partitionKey,
								ex.getMessage());
						return;
					}

					LOGGER.info("Published shipping eventId={} eventType={} correlationId={} topic={} partition={} offset={}",
							message.getEventId(),
							message.getEventType(),
							message.getCorrelationId(),
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().partition(),
							result.getRecordMetadata().offset());
				});
	}

	private ShippingEventMessage buildMessage(String eventType, Shipping shipping, String previousStatus, String correlationId, String partitionKey) {
		ShippingEventMessage message = new ShippingEventMessage();
		message.setEventId(UUID.randomUUID().toString());
		message.setEventType(eventType);
		message.setEventVersion("1.0");
		message.setOccurredAt(OffsetDateTime.now(ZoneOffset.UTC));
		message.setProducer("shipping-service");
		message.setTraceId(UUID.randomUUID().toString());
		message.setCorrelationId(Optional.ofNullable(correlationId)
				.filter(value -> !value.isBlank())
				.orElseGet(() -> Optional.ofNullable(shipping.getOrderId()).map(String::valueOf).orElse(partitionKey)));
		message.setPartitionKey(partitionKey);
		message.setPayload(buildPayload(shipping, previousStatus));
		return message;
	}

	private ShippingEventPayload buildPayload(Shipping shipping, String previousStatus) {
		ShippingEventPayload payload = new ShippingEventPayload();
		payload.setShippingId(shipping.getId() != null ? shipping.getId().toString() : null);
		payload.setOrderId(shipping.getOrderId());
		payload.setStatus(shipping.getStatus() != null ? shipping.getStatus().name() : null);
		payload.setPreviousStatus(previousStatus);
		payload.setTrackingNumber(shipping.getTrackingNumber());
		return payload;
	}

	private String resolvePartitionKey(Shipping shipping) {
		if (shipping.getOrderId() != null) {
			return String.valueOf(shipping.getOrderId());
		}
		if (shipping.getId() != null) {
			return shipping.getId().toString();
		}
		return UUID.randomUUID().toString();
	}
}