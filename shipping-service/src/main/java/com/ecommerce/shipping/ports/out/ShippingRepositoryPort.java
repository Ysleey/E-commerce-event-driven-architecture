package com.ecommerce.shipping.ports.out;

import com.ecommerce.shipping.domain.model.Shipping;

public interface ShippingRepositoryPort {
	Shipping save(Shipping shipping);
}
