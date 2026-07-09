package com.ecommerce.shipping.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {
	private UUID id;
	private UUID orderId;
	private ShippingStatus status;
	private String trackingNumber;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
}
