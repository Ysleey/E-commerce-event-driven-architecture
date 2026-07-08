package com.ecommerce.order.ports.in;

import com.ecommerce.order.domain.model.Order;

public interface UpdateOrderStateUseCase {
    //Ciclo Logístico del Pedido
    Order shipOrder(Long orderId, String trackingNumber);
    Order completeOrder(Long orderId);
    Order cancelOrder(Long orderId, String reason);
}