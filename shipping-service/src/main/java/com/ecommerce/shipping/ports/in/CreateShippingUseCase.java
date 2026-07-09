package com.ecommerce.shipping.ports.in;

import com.ecommerce.shipping.domain.model.Shipping;

public interface CreateShippingUseCase {
	Shipping createPendingShipment(Long orderId, String correlationId);
}
