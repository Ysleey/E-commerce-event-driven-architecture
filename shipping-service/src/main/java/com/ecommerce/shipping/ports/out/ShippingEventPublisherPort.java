package com.ecommerce.shipping.ports.out;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;

public interface ShippingEventPublisherPort {
	void publishShippingCreated(Shipping shipping, String correlationId);

	void publishShippingStatusChanged(Shipping shipping, ShippingStatus previousStatus, String correlationId);
}