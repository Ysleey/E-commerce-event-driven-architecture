package com.ecommerce.order.ports.in;

import com.ecommerce.order.domain.model.Order;

public interface UpdateOrderDetailsUseCase {
    //Modificación de detalles del pedido
    Order updateShippingAddress(Long orderId, String newAddress);
}