package com.ecommerce.shipping.ports.in;

import com.ecommerce.shipping.domain.model.Shipping;
import com.ecommerce.shipping.domain.model.ShippingStatus;

public interface UpdateShippingStatusUseCase {
	Shipping updateShippingStatus(Long orderId, ShippingStatus newStatus, String trackingNumber, String correlationId);
}