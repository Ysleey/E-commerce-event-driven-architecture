package com.ecommerce.shipping.adapter.out.messaging.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingEventPayload {

	private String shippingId;
	private Long orderId;
	private String status;
	private String previousStatus;
	private String trackingNumber;
}