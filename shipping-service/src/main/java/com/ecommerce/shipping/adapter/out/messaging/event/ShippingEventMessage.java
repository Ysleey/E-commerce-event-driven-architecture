package com.ecommerce.shipping.adapter.out.messaging.event;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingEventMessage {

	private String eventId;
	private String eventType;
	private String eventVersion;
	private OffsetDateTime occurredAt;
	private String producer;
	private String traceId;
	private String correlationId;
	private String partitionKey;
	private ShippingEventPayload payload;
}