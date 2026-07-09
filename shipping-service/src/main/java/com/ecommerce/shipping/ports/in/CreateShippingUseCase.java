package com.ecommerce.shipping.ports.in;

import java.util.UUID;

import com.ecommerce.shipping.domain.model.Shipping;

public interface CreateShippingUseCase {
	Shipping createPendingShipment(UUID orderId);
}
